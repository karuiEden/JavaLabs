package ru.mephi.lab2.task14;

import java.util.ArrayList;

public class Network {
    public class Member { // Member is an inner class of Network
        private final String name;
        private final ArrayList<Member> friends = new ArrayList<>();

        public Member(String name) {
            this.name = name;
        }

        public void leave() {
            members.remove(this);
        }

        public void addFriend(Member newFriend) {
            friends.add(newFriend);
        }

        public boolean belongsTo(Network n) {
            return Network.this == n;
        }

        public String toString() {
            StringBuilder result = new StringBuilder(name);
            result.append(" with friends ");
            for (Member friend : friends) {
                result.append(friend.name);
                result.append(", ");
            }
            return result.subSequence(0, result.length() - 2).toString();
        }
    }

    private final ArrayList<Member> members = new ArrayList<>();

    public Member enroll(String name) {
        Member newMember = new Member(name);
        members.add(newMember);
        return newMember;
    }

    public String toString() {
        return members.toString();
    }
}

/**
 * Compiled from "Network.java"
 * public class ru.mephi.lab2.task14.Network$Member {
 *   private java.lang.String name;
 *   private java.util.ArrayList<ru.mephi.lab2.task14.Network$Member> friends;
 *   final ru.mephi.lab2.task14.Network this$0; // Ссылка на внешний объект
 *   public ru.mephi.lab2.task14.Network$Member(ru.mephi.lab2.task14.Network // принимает в конструкторе объект Network, java.lang.String);
 *   public void leave();
 *   public void addFriend(ru.mephi.lab2.task14.Network$Member);
 *   public boolean belongsTo(ru.mephi.lab2.task14.Network);
 *   public java.lang.String toString();
 * }
 */