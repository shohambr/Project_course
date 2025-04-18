package ServiceLayer;

import DomainLayer.IJobRepository;
import DomainLayer.Roles.Jobs.Job;
import DomainLayer.Roles.Jobs.Managing;
import DomainLayer.Roles.Jobs.Ownership;
import DomainLayer.Roles.RegisteredUser;
import DomainLayer.Roles.SystemManager;
import DomainLayer.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JobServiceTest {
    
    private IJobRepository jobRepository;
    private StoreService storeService;
    private JobService jobService;
    
    @BeforeEach
    void setUp() {
        jobRepository = Mockito.mock(IJobRepository.class);
        storeService = Mockito.mock(StoreService.class);
        jobService = new JobService(jobRepository, storeService);
    }
    
    @Test
    void createStore_ShouldCreateStoreAndAddFounderJob() {
        // Arrange
        RegisteredUser founder = Mockito.mock(RegisteredUser.class);
        when(founder.getID()).thenReturn("user1");
        
        Store store = Mockito.mock(Store.class);
        when(store.getId()).thenReturn("store1");
        when(storeService.createStore()).thenReturn(store);
        
        // Act
        jobService.createStore(founder);
        
        // Assert
        verify(storeService).createStore();
        verify(jobRepository).addJob(eq("user1"), eq("store1"), any(Ownership.class));
    }
    
    @Test
    void userIsOwnerOfStore_ShouldReturnTrue_WhenUserIsOwner() {
        // Arrange
        String storeId = "store1";
        String userId = "user1";
        
        LinkedList<Job> jobs = new LinkedList<>();
        Store store = Mockito.mock(Store.class);
        when(store.getId()).thenReturn(storeId);
        
        RegisteredUser user = Mockito.mock(RegisteredUser.class);
        when(user.getID()).thenReturn(userId);
        
        Ownership ownershipJob = Mockito.mock(Ownership.class);
        when(ownershipJob.getStore()).thenReturn(store);
        
        jobs.add(ownershipJob);
        
        when(jobRepository.getJobsByUser(userId)).thenReturn(jobs);
        
        // Act
        boolean result = jobService.UserIsOwnerOfStore(storeId, userId);
        
        // Assert
        assertTrue(result);
        verify(jobRepository).getJobsByUser(userId);
    }
    
    @Test
    void userIsOwnerOfStore_ShouldReturnFalse_WhenUserIsNotOwner() {
        // Arrange
        String storeId = "store1";
        String userId = "user1";
        
        LinkedList<Job> jobs = new LinkedList<>();
        Store store = Mockito.mock(Store.class);
        when(store.getId()).thenReturn("different-store");
        
        RegisteredUser user = Mockito.mock(RegisteredUser.class);
        when(user.getID()).thenReturn(userId);
        
        Ownership ownershipJob = Mockito.mock(Ownership.class);
        when(ownershipJob.getStore()).thenReturn(store);
        
        jobs.add(ownershipJob);
        
        when(jobRepository.getJobsByUser(userId)).thenReturn(jobs);
        
        // Act
        boolean result = jobService.UserIsOwnerOfStore(storeId, userId);
        
        // Assert
        assertFalse(result);
        verify(jobRepository).getJobsByUser(userId);
    }
    
    @Test
    void userIsManagerOfStore_ShouldReturnTrue_WhenUserIsManager() {
        // Arrange
        String storeId = "store1";
        String userId = "user1";
        
        LinkedList<Job> jobs = new LinkedList<>();
        Store store = Mockito.mock(Store.class);
        when(store.getId()).thenReturn(storeId);
        
        RegisteredUser user = Mockito.mock(RegisteredUser.class);
        when(user.getID()).thenReturn(userId);
        
        Managing managingJob = Mockito.mock(Managing.class);
        when(managingJob.getStore()).thenReturn(store);
        
        jobs.add(managingJob);
        
        when(jobRepository.getJobsByUser(userId)).thenReturn(jobs);
        
        // Act
        boolean result = jobService.UserIsManagerOfStore(storeId, userId);
        
        // Assert
        assertTrue(result);
        verify(jobRepository).getJobsByUser(userId);
    }
    
    @Test
    void getSpecificOwnershipJob_ShouldReturnJob_WhenUserIsOwner() {
        // Arrange
        String storeId = "store1";
        String userId = "user1";
        
        LinkedList<Job> jobs = new LinkedList<>();
        Store store = Mockito.mock(Store.class);
        when(store.getId()).thenReturn(storeId);
        
        RegisteredUser user = Mockito.mock(RegisteredUser.class);
        when(user.getID()).thenReturn(userId);
        
        Ownership ownershipJob = Mockito.mock(Ownership.class);
        when(ownershipJob.getStore()).thenReturn(store);
        
        jobs.add(ownershipJob);
        
        when(jobRepository.getJobsByUser(userId)).thenReturn(jobs);
        
        // Act
        Ownership result = jobService.getSpecificOwnershipJob(storeId, userId);
        
        // Assert
        assertSame(ownershipJob, result);
        verify(jobRepository).getJobsByUser(userId);
    }
    
    @Test
    void getSpecificOwnershipJob_ShouldThrowException_WhenJobNotFound() {
        // Arrange
        String storeId = "store1";
        String userId = "user1";
        
        LinkedList<Job> jobs = new LinkedList<>();
        
        when(jobRepository.getJobsByUser(userId)).thenReturn(jobs);
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            jobService.getSpecificOwnershipJob(storeId, userId);
        });
        
        assertEquals("ownership job wasn't found", exception.getMessage());
    }
    
    @Test
    void closeStore_ShouldCloseStore_WhenCalledByFounder() {
        // Arrange
        String storeId = "store1";
        String userId = "user1";
        
        Store store = Mockito.mock(Store.class);
        when(store.getId()).thenReturn(storeId);
        
        RegisteredUser founder = Mockito.mock(RegisteredUser.class);
        when(founder.getID()).thenReturn(userId);
        
        Ownership ownershipJob = Mockito.mock(Ownership.class);
        when(ownershipJob.getStore()).thenReturn(store);
        when(ownershipJob.isFounder()).thenReturn(true);
        
        LinkedList<Job> jobs = new LinkedList<>();
        jobs.add(ownershipJob);
        
        when(jobRepository.getJobsByUser(userId)).thenReturn(jobs);
        
        // Act
        jobService.closeStore(store, founder);
        
        // Assert
        verify(ownershipJob).closeStore();
        verify(store).closeTheStore();
    }
    
    @Test
    void closeStoreAsSystemManager_ShouldCloseStore() {
        // Arrange
        String storeId = "store1";
        Store store = Mockito.mock(Store.class);
        when(store.getId()).thenReturn(storeId);
        
        SystemManager systemManager = Mockito.mock(SystemManager.class);
        
        LinkedList<Job> storeJobs = new LinkedList<>();
        Ownership founderJob = Mockito.mock(Ownership.class);
        when(founderJob.isFounder()).thenReturn(true);
        storeJobs.add(founderJob);
        
        when(jobRepository.getJobsByStore(storeId)).thenReturn(storeJobs);
        
        // Act
        jobService.closeStoreAsSystemManager(store, systemManager);
        
        // Assert
        verify(store).closeTheStore();
        verify(founderJob).delete();
    }
    
    @Test
    void openStore_ShouldOpenStore_WhenCalledByFounder() {
        // Arrange
        String storeId = "store1";
        String userId = "user1";
        
        Store store = Mockito.mock(Store.class);
        when(store.getId()).thenReturn(storeId);
        
        RegisteredUser founder = Mockito.mock(RegisteredUser.class);
        when(founder.getID()).thenReturn(userId);
        
        Ownership ownershipJob = Mockito.mock(Ownership.class);
        when(ownershipJob.getStore()).thenReturn(store);
        when(ownershipJob.isFounder()).thenReturn(true);
        
        LinkedList<Job> jobs = new LinkedList<>();
        jobs.add(ownershipJob);
        
        when(jobRepository.getJobsByUser(userId)).thenReturn(jobs);
        
        // Act
        jobService.openStore(store, founder);
        
        // Assert
        verify(ownershipJob).reOpenStore();
        verify(store).openTheStore();
    }
    
    @Test
    void getInfoJobsInStore_ShouldReturnJobInfo_WhenUserIsOwner() {
        // Arrange
        String storeId = "store1";
        String userId = "user1";
        String expectedInfo = "Job info string";
        
        Store store = Mockito.mock(Store.class);
        when(store.getId()).thenReturn(storeId);
        
        RegisteredUser owner = Mockito.mock(RegisteredUser.class);
        when(owner.getID()).thenReturn(userId);
        
        Ownership ownershipJob = Mockito.mock(Ownership.class);
        when(ownershipJob.getStore()).thenReturn(store);
        when(ownershipJob.toString()).thenReturn(expectedInfo);
        when(ownershipJob.isFounder()).thenReturn(true);
        
        LinkedList<Job> userJobs = new LinkedList<>();
        userJobs.add(ownershipJob);
        when(jobRepository.getJobsByUser(userId)).thenReturn(userJobs);
        
        LinkedList<Job> storeJobs = new LinkedList<>();
        storeJobs.add(ownershipJob);
        when(jobRepository.getJobsByStore(storeId)).thenReturn(storeJobs);
        
        // Act
        String result = jobService.getInfoJobsInStore(store, owner);
        
        // Assert
        assertEquals(expectedInfo, result);
        verify(jobRepository).getJobsByStore(storeId);
    }
}
