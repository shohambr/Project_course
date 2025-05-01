package DomainLayer;

import DomainLayer.Roles.Jobs.Job;

import java.util.LinkedList;

public interface IJobRepository {
    public LinkedList<Job> getJobsByUser(String userID);
    public LinkedList<Job> getJobsByStore(String storeID);
    public boolean userHasJob(String RegisteredUserID, Job job);
    public boolean storeHaveJob(String storeID, Job job);
    public void removeJob(String RegisteredUserID, String storeID, Job job);
    public void addJob(String RegisteredUserID, String storeID, Job job);
}
