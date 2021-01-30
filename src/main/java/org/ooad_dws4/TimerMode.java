package org.ooad_dws4;

import java.util.HashMap;

public class TimerMode extends Mode {

    private Timer timer;

    private int field;
    private int hour, minute, second;
    private long maxValue = 359999000L;

    public TimerMode(boolean isActivation) {

        this.timer = new Timer();
        this.state = 0;
        this.hour = 0;
        this.minute = 0;
        this.second = 0;
        this.field = 3;
        this.isActivate = isActivation;
        this.modeName = "TIMER";
    }

    @Override
    public Message getModeData() {
        msecTohhmmss(this.timer.getDeadlineData());
        return makeView();
    }

    @Override
    public Message update(long systemTime) {
        if (state == 2) {
            timer.runTimer();
            if (timer.getDeadlineData() < 0)
                changeState("DEFAULT");
        }
        return null;
    }

    @Override
    public Message update(long systemTime, boolean currentMode) {
        update(systemTime);
        if (!currentMode) return null;
        msecTohhmmss(timer.getDeadlineData());
        return makeView();
    }

    @Override
    public Message modeModify(int event) {

        if (this.state == 0) {
            switch (event) {
                case 3:
                    return startTimer();
                case 5:
                    return changeTimerTime();
            }
        } else if (this.state == 1) {
            switch (event) {
                case 1:
                case 5:
                    return saveTimer();
                case 2:
                    return changeField();
                case 3:
                case 4:
                    return changeValue(event == 4 ? 1 : -1); // +
            }
        } else if (this.state == 2) { // when running
            if (event == 3)
                return pauseTimer();
        } else if (this.state == 3) { // when pause
            switch (event) {
                case 2:
                    return resetTimer();
                case 3:
                    return resumeTimer();
            }
        }
        return null;
    }

    private Message changeField() {
        field++;
        if (field > 5)
            field = 3;
        HashMap<String, String> arg = new HashMap<String, String>();
        arg.put("blink", Integer.toString(field));
        return new Message(11, "updateView", arg);
    }

    private Message changeValue(int sign) {
        long value = this.timer.getDeadlineData();
        long stepSize = 0;
        if (field == 3)
            stepSize = 1000 * 60 * 60;
        else if (field == 4)
            stepSize = 1000 * 60;
        else if (field == 5)
            stepSize = 1000;
        value += stepSize * sign;
        if (value < 0 | value > maxValue)
            return null;
        timer.setDeadlineData(value);
        msecTohhmmss(timer.getDeadlineData());
        return makeView();

    }

    private Message resetTimer() {
        timer.reset();
        changeState("DEFAULT");
        long timerTime = timer.getDeadlineData();
        msecTohhmmss(timerTime);
        return makeView();
    }

    private Message startTimer() {
        if (timer.getDeadlineData() == 0) return null;
        changeState("RUN");
        HashMap<String, String> arg = new HashMap<String, String>();
        arg.put("0", "ringing");
        arg.put("1", Long.toString(timer.getDeadlineData()));
        arg.put("2", "351");
        return new Message(22, "updateTimerEvent", arg);
    }

    private Message changeTimerTime() {
        // change state edit
        changeState("EDIT");
        Message message = makeView();
        message.getArg().put("blink", Integer.toString(field));
        return message;

    }

    private Message saveTimer() {
        changeState("DEFAULT");
        long timerTime = timer.getDeadlineData();
        field = 3;
        msecTohhmmss(timerTime);
        Message message = makeView();
        message.getArg().put("blink", null);
        return message;
    }

    private Message pauseTimer() {
        changeState("PAUSE");
        HashMap<String, String> arg = new HashMap<String, String>();
        arg.put("0", "off");
        arg.put("2", "351");
        return new Message(22, "updateTimerEvent", arg);
    }

    private Message resumeTimer() {
        return startTimer();
    }



    private void msecTohhmmss(long timerTime) {
        this.second = (int) (timerTime / 1000) % 60;
        this.minute = (int) ((timerTime) / (1000 * 60) % 60);
        this.hour = (int) ((timerTime) / (1000 * 60 * 60) % 24);
    }

    private void changeState(String state) {
        if ("DEFAULT".equals(state))
            this.state = 0;
        else if ("EDIT".equals(state))
            this.state = 1;
        else if ("RUN".equals(state))
            this.state = 2;
        else if ("PAUSE".equals(state))
            this.state = 3;

    }

    private Message makeView() {
        HashMap<String, String> arg = new HashMap<>();
        String state;
        if (this.state == 1)
            state = "EDT";
        else if (this.state == 2)
            state = "RUN";
        else
            state = "OFF";
        arg.put("0", state);
        arg.put("3", makeTimeForm(this.hour, this.minute, this.second));
        arg.put("4", "  TIMER   ");
        return new Message(11, "updateView", arg);
    }

    private String makeTimeForm(int hour, int minute, int second) {
        char result[] = "00|0000".toCharArray();
        char hourChar[] = Integer.toString(hour).toCharArray();
        char minChar[] = Integer.toString(minute).toCharArray();
        char secChar[] = Integer.toString(second).toCharArray();
        for (int i = 0; i < hourChar.length; i++)
            result[2 - hourChar.length + i] = hourChar[i];
        for (int i = 0; i < minChar.length; i++)
            result[5 - minChar.length + i] = minChar[i];
        for (int i = 0; i < secChar.length; i++)
            result[7 - secChar.length + i] = secChar[i];
        return new String(result);
    }
}