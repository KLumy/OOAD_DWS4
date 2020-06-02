package org.ooad_dws4;

import java.util.HashMap;

public class WoldTimeMode extends Mode {
    private City[] cities;
    private int thisTimeZone;

    public WoldTimeMode(boolean isActivation) {
        this.cities = new City[4];
        for (int i = 0; i < 4; i++) {
            this.cities[i] = new City(false, i);
        }
        this.thisTimeZone = 3;
        /* NY : GMT-4
         * London: GMT+1
         * Paris : GMT+2
         * Seoul : GMT+9 */
        this.isActivate = isActivation;
        this.modeName = "WORLD TIME";
    }

    public void changeCityIndex(int value) {
        if (this.thisTimeZone == 0 && value == -1) {
            thisTimeZone = 3;
            return;
        }
        this.thisTimeZone = (this.thisTimeZone + value) % 4;
    }

    private int calcOffsetDif() {
        return thisTimeZone * 2;
    }

    public void changeCity(int value) {
        // TODO implement here
    }

    private Message setCity() {
        int offset = cities[thisTimeZone].getOffset(thisTimeZone);
        return null;
    }

    private void toggleActivation() {
    }

    public Message getModeData() {
        return null;
    }


    @Override
    public Message modeModify(int event) {
        switch (event) {
            case 2:
                return setCity();
            case 3:
                return showCity(-1);
            case 4:
                return showCity(1);
        }
        return null;
    }

    private Message showCity(int value) {
        changeCityIndex(value);
        int timeZone = cities[thisTimeZone].getTimeZoneData();
        HashMap<String, String> arg = new HashMap<String, String>();
        makeUpdateViewArg(arg, calcOffsetDif(), null);
        return new Message(11, "updateView", arg);
    }

    @Override
    public void changeState(int state) {
    }

    @Override
    public Message update(long systemTime) {
        return null;
    }

    @Override
    public Message update(long systemTime, boolean currentMode) {
        return null;
    }

    @Override
    public Message saveActivation() {
        return null;
    }

    @Override
    public boolean receiveMessage(Message msg) {
        return false;
    }

    /* personally added */
    private void makeUpdateViewArg(HashMap<String, String> arg, long timeDiff, String blink) { //f
        arg.put("0", Long.toString(timeDiff));
        arg.put("1", null); /* should be added in mode manager */
        arg.put("3", Long.toString(timeDiff));
        arg.put("4", Long.toString(timeDiff));
        arg.put("blink", blink);
    }
    /* personally added */
}