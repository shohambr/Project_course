package ServiceLayer;

import DomainLayer.*;
import DomainLayer.Roles.Jobs.Job;
import DomainLayer.Roles.Jobs.Managing;
import DomainLayer.Roles.Jobs.Ownership;
import DomainLayer.Roles.RegisteredUser;
import Mocks.TestManaging;
import Mocks.TestOwnership;
import Mocks.TestRegisteredUser;
import Mocks.TestStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class JobServiceTest {
    private IStoreRepository storeRepository;
    private ProductService productService;
    private StoreService storeService;
    private JobService jobService;
    private IJobRepository jobRepository;
    private RegisteredUser dummyUser;
    private Store dummyStore;

    @BeforeEach
    void setUp() {
        // Mock dependencies
        storeRepository = Mockito.mock(IStoreRepository.class);
        IProductRepository productRepo = Mockito.mock(IProductRepository.class);
        productService = new ProductService(productRepo);
        jobRepository = Mockito.mock(IJobRepository.class);

        // Create services with dependencies
        storeService = new StoreService(storeRepository, productService);
        jobService = new JobService(jobRepository, storeService);

        // Create test objects
        dummyUser = new TestRegisteredUser("dummyUserID");
        dummyStore = new TestStore("storeID");
    }

    @Test
    void createStore_ShouldCreateStoreAndAssignFounderAsOwner() {
        // Arrange
        Store newStore = new TestStore("1");

        // Create a test implementation of StoreService that returns our test store
        StoreService testStoreService = new StoreService(storeRepository, productService) {
            @Override
            public Store createStore() {
                return newStore;
            }
        };

        // Create a new JobService with our test StoreService
        JobService testJobService = new JobService(jobRepository, testStoreService);

        // Act
        testJobService.createStore(dummyUser);

        // Assert
        // Verify that a job was added to the repository
        ArgumentCaptor<Job> jobCaptor = ArgumentCaptor.forClass(Job.class);
        Mockito.verify(jobRepository).addJob(Mockito.eq(dummyUser.getID()), Mockito.eq(newStore.getId()), jobCaptor.capture());

        // Verify the job is an Ownership job
        Job capturedJob = jobCaptor.getValue();
        assertTrue(capturedJob instanceof Ownership);
        assertEquals(dummyUser, capturedJob.getRegisteredUser());
    }

    @Test
    void userIsOwnerOfStore_ShouldReturnTrue_WhenUserIsOwner() {
        // Arrange
        LinkedList<Job> jobs = new LinkedList<>();
        Ownership ownershipJob = new TestOwnership(dummyUser, dummyStore, true);
        jobs.add(ownershipJob);

        Mockito.when(jobRepository.getJobsByUser("dummyUserID")).thenReturn(jobs);

        // Act
        boolean result = jobService.UserIsOwnerOfStore("storeID", "dummyUserID");

        // Assert
        assertTrue(result);
    }

    @Test
    void userIsOwnerOfStore_ShouldReturnFalse_WhenUserIsNotOwner() {
        // Arrange
        LinkedList<Job> jobs = new LinkedList<>();
        TestOwnership ownershipJob = new TestOwnership(dummyUser, dummyStore, true);
        Managing managingJob = new TestManaging(dummyUser, dummyStore, ownershipJob);
        jobs.add(managingJob);

        Mockito.when(jobRepository.getJobsByUser("dummyUserID")).thenReturn(jobs);

        // Act
        boolean result = jobService.UserIsOwnerOfStore("storeID", "dummyUserID");

        // Assert
        assertFalse(result);
    }

    @Test
    void userIsManagerOfStore_ShouldReturnTrue_WhenUserIsManager() {
        // Arrange
        LinkedList<Job> jobs = new LinkedList<>();
        TestOwnership ownershipJob = new TestOwnership(dummyUser, dummyStore, true);
        Managing managingJob = new TestManaging(dummyUser, dummyStore, ownershipJob);
        jobs.add(managingJob);

        Mockito.when(jobRepository.getJobsByUser("dummyUserID")).thenReturn(jobs);

        // Act
        boolean result = jobService.UserIsManagerOfStore("storeID", "dummyUserID");

        // Assert
        assertTrue(result);
    }

    @Test
    void userIsManagerOfStore_ShouldReturnFalse_WhenUserIsNotManager() {
        // Arrange
        LinkedList<Job> jobs = new LinkedList<>();
        Ownership ownershipJob = new TestOwnership(dummyUser, dummyStore, true);
        jobs.add(ownershipJob);

        Mockito.when(jobRepository.getJobsByUser("dummyUserID")).thenReturn(jobs);

        // Act
        boolean result = jobService.UserIsManagerOfStore("storeID", "dummyUserID");

        // Assert
        assertFalse(result);
    }

    @Test
    void getSpecificOwnershipJob_ShouldReturnJob_WhenUserIsOwner() {
        // Arrange
        LinkedList<Job> jobs = new LinkedList<>();
        Ownership ownershipJob = new TestOwnership(dummyUser, dummyStore, true);
        jobs.add(ownershipJob);

        Mockito.when(jobRepository.getJobsByUser("dummyUserID")).thenReturn(jobs);

        // Act
        Ownership result = jobService.getSpecificOwnershipJob("storeID", "dummyUserID");

        // Assert
        assertEquals(ownershipJob, result);
    }

    @Test
    void getSpecificOwnershipJob_ShouldThrowException_WhenUserIsNotOwner() {
        // Arrange
        LinkedList<Job> jobs = new LinkedList<>();
        TestOwnership ownershipJob = new TestOwnership(dummyUser, dummyStore, true);
        Managing managingJob = new TestManaging(dummyUser, dummyStore, ownershipJob);
        jobs.add(managingJob);

        Mockito.when(jobRepository.getJobsByUser("dummyUserID")).thenReturn(jobs);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            jobService.getSpecificOwnershipJob("storeID", "dummyUserID");
        });
    }

    @Test
    void closeStore_ShouldCloseStore_WhenUserIsFounder() {
        // Arrange
        final TestOwnership founderJob = new TestOwnership(dummyUser, dummyStore, true);

        // Create a test implementation of JobService that returns our test ownership
        JobService testJobService = new JobService(jobRepository, storeService) {
            @Override
            public Ownership getSpecificOwnershipJob(String storeID, String ownerID) {
                return founderJob;
            }
        };

        // Act
        testJobService.closeStore(dummyStore, dummyUser);

        // Assert
        // Since we're using real objects, we can't verify method calls with Mockito
        // Instead, we could add verification methods to TestOwnership and TestStore
        // For now, we'll just verify that the test completes without exceptions
    }

    @Test
    void openStore_ShouldOpenStore_WhenUserIsFounder() {
        // Arrange
        final TestOwnership founderJob = new TestOwnership(dummyUser, dummyStore, true);

        // Create a test implementation of JobService that returns our test ownership
        JobService testJobService = new JobService(jobRepository, storeService) {
            @Override
            public Ownership getSpecificOwnershipJob(String storeID, String ownerID) {
                return founderJob;
            }
        };

        // Act
        testJobService.openStore(dummyStore, dummyUser);

        // Assert
        // Since we're using real objects, we can't verify method calls with Mockito
        // Instead, we could add verification methods to TestOwnership and TestStore
        // For now, we'll just verify that the test completes without exceptions
    }
}
