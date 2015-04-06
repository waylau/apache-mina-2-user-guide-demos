package com.waylau.mina.demo.chat;

/**
 * Encapsulates a chat command. Use {@link #valueOf(String)} to create an
 * instance given a command string.
 *
 * @author waylau.com
 * @date 2015-4-6
 */
public class ChatCommand {
    public static final int LOGIN = 0;

    public static final int QUIT = 1;

    public static final int BROADCAST = 2;

    private final int num;

    private ChatCommand(int num) {
        this.num = num;
    }

    public int toInt() {
        return num;
    }

    public static ChatCommand valueOf(String s) {
        s = s.toUpperCase();
        if ("LOGIN".equals(s)) {
            return new ChatCommand(LOGIN);
        }
        if ("QUIT".equals(s)) {
            return new ChatCommand(QUIT);
        }
        if ("BROADCAST".equals(s)) {
            return new ChatCommand(BROADCAST);
        }

        throw new IllegalArgumentException("Unrecognized command: " + s);
    }
}
