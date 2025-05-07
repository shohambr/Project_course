package ServiceLayer;

import DomainLayer.IJobRepository;
import DomainLayer.IStoreRepository;
import DomainLayer.IToken;
import DomainLayer.IUserRepository;
import DomainLayer.Roles.Jobs.Job;
import DomainLayer.Roles.Jobs.Managing;
import DomainLayer.Roles.Jobs.Ownership;
import DomainLayer.Roles.RegisteredUser;
import DomainLayer.Roles.SystemManager;
import DomainLayer.Store;
import DomainLayer.DomainServices.OpenStore;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

@Service
public class JobService {
    private final IJobRepository JobRepository;
    private final IStoreRepository storeRepository;
    private final IUserRepository userRepository;
    private final StoreService storeService;
    private final IToken tokenService;
    private final OpenStore openStoreService;

    public JobService(IJobRepository jobRepository, StoreService storeService , IStoreRepository storeRepository, IToken token , IUserRepository userRepository) {
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
        this.tokenService = token;
        this.openStoreService = new OpenStore(token, storeRepository, userRepository);
        this.JobRepository = jobRepository;
        this.storeService = storeService;
    }

    // public void createStore(String token) {
    //     Store createdStore = openStoreService.openStore(token);
    //     Job founderJob = new Ownership(founder,createdStore);
    //     this.JobRepository.addJob(founder.getID(), createdStore.getId(), founderJob);
    // }

    // public boolean UserIsOwnerOfStore(String storeID, String OwnerID){
    //     //check that the user isn't already appointed as an owner:
    //     LinkedList<Job> jobs = this.JobRepository.getJobsByUser(OwnerID);
    //     for(Job j: jobs){
    //         if ((j.getStore().getId().equals(storeID))&&(j instanceof Ownership)){
    //             return true;
    //         }
    //     }
    //     return false;
    // }
    // public boolean UserIsManagerOfStore(String storeID, String ManagerID){
    //     //check that the user isn't already appointed as an owner:
    //     LinkedList<Job> jobs = this.JobRepository.getJobsByUser(ManagerID);
    //     for(Job j: jobs){
    //         if ((j.getStore().getId().equals(storeID))&&(j instanceof Managing)){
    //             return true;
    //         }
    //     }
    //     return false;
    // }
    // public Ownership getSpecificOwnershipJob(String storeID, String ownerID){
    //     if (UserIsOwnerOfStore(storeID,ownerID)){
    //         LinkedList<Job> jobs = this.JobRepository.getJobsByUser(ownerID);
    //         for(Job j: jobs){
    //             if ((j.getStore().getId().equals(storeID))&&(j instanceof Ownership)){
    //                 return (Ownership) j;
    //             }
    //         }
    //     }
    //     throw new RuntimeException("ownership job wasn't found");
    // }
    // public Managing getSpecificManagmentJob(String storeID, String managerID){
    //     if (UserIsManagerOfStore(storeID,managerID)){
    //         LinkedList<Job> jobs = this.JobRepository.getJobsByUser(managerID);
    //         for(Job j: jobs){
    //             if ((j.getStore().getId().equals(storeID))&&(j instanceof Managing)){
    //                 return (Managing) j;
    //             }
    //         }
    //     }
    //     throw new RuntimeException("management job wasn't found");
    // }
    // private Job getSpecificJob(String storeID, String userID){
    //     if (UserIsOwnerOfStore(storeID,userID)||UserIsManagerOfStore(storeID,userID)){
    //         LinkedList<Job> jobs = this.JobRepository.getJobsByUser(userID);
    //         for(Job j: jobs){
    //             if ((j.getStore().getId().equals(storeID))){
    //                 return (Job) j;
    //             }
    //         }
    //     }
    //     throw new RuntimeException("job wasn't found");
    // }
    // public void addNewOwnerToStore(Store store, RegisteredUser oldOwner, RegisteredUser newOwner) {
    //     //make sure that the newOwner isn't an owner and that the oldOwner is still an owner
    //     if(!UserIsOwnerOfStore(store.getId(), newOwner.getID()) && UserIsOwnerOfStore(store.getId(),oldOwner.getID())) {
    //         //make sure that if he was a manager for the store before his management job will be deleted
    //         if (UserIsManagerOfStore(store.getId(),newOwner.getID())){
    //             this.deleteJob(store.getId(),newOwner.getID(),getSpecificManagmentJob(store.getId(), newOwner.getID()));
    //         }
    //         Ownership newOwnership = new Ownership(store,newOwner,getSpecificOwnershipJob(store.getId(), oldOwner.getID()));
    //         this.addJob(store.getId(),newOwner.getID(),newOwnership);
    //         return;
    //     }
    //     throw new RuntimeException("the owner wasn't added to the store");
    // }
    // private void addJob(String storeId, String userID, Job jobToAdd) {
    //     this.JobRepository.addJob(storeId, userID, jobToAdd);
    // }
    // private void deleteJob(String storeID, String userID, Job jobToDelete) {
    //     jobToDelete.delete();
    //     this.JobRepository.removeJob(storeID,userID,jobToDelete);
    // }
    // public void fireFromMyStore(Store store, RegisteredUser superior, RegisteredUser subordinate) {
    //     //make sure the superior is an owner
    //     Job subordinateJob;
    //     Job superiorJob;
    //     if(this.UserIsOwnerOfStore(store.getId(), superior.getID())){
    //         superiorJob = this.getSpecificOwnershipJob(store.getId(), superior.getID());
    //         if (this.UserIsManagerOfStore(store.getId(),subordinate.getID())){
    //             subordinateJob = this.getSpecificManagmentJob(store.getId(),subordinate.getID());
    //         } else if (this.UserIsOwnerOfStore(store.getId(), subordinate.getID())) {
    //             subordinateJob = this.getSpecificOwnershipJob(store.getId(),subordinate.getID());
    //         } else {
    //             throw new RuntimeException("the user is not assossiated with the store");
    //         }
    //         if (this.checkThatIsSuperior(superiorJob,subordinateJob)){
    //             this.deleteJob(store.getId(), subordinate.getID(), subordinateJob);
    //         }
    //     }
    // }
    // private boolean checkThatIsSuperior(Job superiorJob, Job subordinateJob) {
    //     return superiorJob.findSubordinate(subordinateJob);
    // }
    // public void addNewManagerToStore(Store store, RegisteredUser oldOwner, RegisteredUser newManager,boolean[] permissions) {
    //     //make sure that the newManager isnt an owner and that the oldOwner is still an owner
    //     if(!UserIsOwnerOfStore(store.getId(), newManager.getID()) && UserIsOwnerOfStore(store.getId(),oldOwner.getID())) {
    //         //make sure the if he was a manager for the store before his managment job will be deleted
    //         if (!UserIsManagerOfStore(store.getId(), newManager.getID())) {
    //             Managing newManaging = new Managing(store, newManager, getSpecificOwnershipJob(store.getId(), oldOwner.getID()), permissions);
    //             this.addJob(store.getId(), newManager.getID(), newManaging);
    //             return;
    //         }
    //         throw new RuntimeException("the manager is already a manager of the store");
    //     }
    //     throw new RuntimeException("the manager wasn't added to the store");
    // }
    // public void changeManagerPermissions(Store store, RegisteredUser owner, RegisteredUser manager, boolean[] permissions) {
    //     Managing managerJob = getSpecificManagmentJob(store.getId(), manager.getID());
    //     Ownership ownerJob = getSpecificOwnershipJob(store.getId(), owner.getID());
    //     if (checkThatIsSuperior(ownerJob,managerJob)){
    //         managerJob.changePermissions(permissions);
    //         return;
    //     }
    //     throw new RuntimeException("the manager is not the owner's subordinate");
    // }
    // public void closeStore(Store store, RegisteredUser founder) {
    //     Ownership founderJob = getSpecificOwnershipJob(store.getId(), founder.getID());
    //     if (founderJob.isFounder()) {
    //         founderJob.closeStore();
    //         founderJob.getStore().closeTheStore();
    //     }
    // }
    // public void closeStoreAsSystemManager(Store store, SystemManager systemManager) {
    //     LinkedList<Job> allJobsInStore = JobRepository.getJobsByStore(store.getId());
    //     store.closeTheStore();
    //     for (Job job : allJobsInStore) {
    //         if (job.isFounder()) job.delete();
    //     }
    // }
    // public void openStore(Store store, RegisteredUser founder) {
    //     Ownership founderJob = getSpecificOwnershipJob(store.getId(), founder.getID());
    //     if (founderJob.isFounder()) {
    //         founderJob.reOpenStore();
    //         founderJob.getStore().openTheStore();
    //     }
    // }
    // public String getInfoJobsInStore(Store store, RegisteredUser owner) {
    //     if (UserIsOwnerOfStore(store.getId(), owner.getID())) {
    //         LinkedList<Job> jobsInStore = JobRepository.getJobsByStore(store.getId());
    //         for (Job job : jobsInStore) {
    //             if (job.isFounder()) return job.toString();
    //         }
    //         return null;
    //     }
    //     throw new RuntimeException("the user is not an owner of the store");
    // }
    // public String getInfoOrdersInStore(Store store, RegisteredUser owner) {
    //     if (UserIsOwnerOfStore(store.getId(), owner.getID())) {
    //         Ownership ownerJob = getSpecificOwnershipJob(store.getId(), owner.getID());
    //         return ownerJob.getOrderHistory();
    //     }
    //     throw new RuntimeException("the user is not an owner of the store");
    // }

    // public String respondToBuyer(Store store, RegisteredUser owner, String query) {
    //     Ownership ownership = getSpecificOwnershipJob(store.getId(), owner.getID());
    //     return ownership.answerCustomersQuery(query);
    // }
}
