package org.example;

public enum Room {
    ROOM1, ROOM2;
    public int getID (Room room) {
        if (room == ROOM1) {
            return 1;
        } else if (room == ROOM2) {
            return 2;
        }
        return -1;
    }
}
