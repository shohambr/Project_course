package DomainLayer.Roles.Jobs;

import DomainLayer.Roles.RegisteredUser;
import DomainLayer.Store;
import jdk.internal.org.jline.terminal.TerminalBuilder;

import java.util.LinkedList;
import java.util.Scanner;

public class Ownership extends Job {
    //the store founder constructor
    public Ownership(RegisteredUser founder, Store newStore) {
        super(founder);
        mySubordinates = new LinkedList<>();
        this.myStore = new Store();
        founder.getJobs().add(this);
    }
    public Ownership(Store store, RegisteredUser me, Ownership jobGiver) {
        super(store,me,jobGiver);
        this.mySubordinates = new LinkedList<>();
        jobGiver.mySubordinates.add(this);
    }
    public boolean findSubordinate(Job subordinateJob) {
        for (Job j: mySubordinates){
            if (j == subordinateJob){
                return true;
            } else {
                return j.findSubordinate(subordinateJob);
            }
        }
        return false;
    }
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Ownership: \n");
        sb.append(super.toString());
        return sb.toString();
    }

    public String getOrderHistory() {
        return myStore.getOrderHistory();
    }

    public String answerCustomersQuery(String query) {
        return "answerCustomersQuery in class ownership not implemented yet";
    }
}