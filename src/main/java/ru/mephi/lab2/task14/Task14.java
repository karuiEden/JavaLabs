package ru.mephi.lab2.task14;



public class Task14 {
    static void main() {
        Network myFace = new Network();
        Network tooter = new Network();
        Network.Member fred = myFace.enroll("Fred");
        Network.Member wilma = myFace.new Member("Wilma");
        // An object, but not enrolled
        // Make the constructor private to avoid this
        fred.addFriend(wilma);

        Network.Member barney = tooter.enroll("Barney");
        fred.addFriend(barney);
        System.out.println(myFace);
        // If it shouldn't be possible to add a friend
        // from another network, call belongsTo
        System.out.println(barney.belongsTo(myFace));
    }
}
