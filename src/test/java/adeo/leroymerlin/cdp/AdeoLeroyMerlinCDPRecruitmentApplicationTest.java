package adeo.leroymerlin.cdp;


import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdeoLeroyMerlinCDPRecruitmentApplicationTest {

  @Autowired
  private AdeoLeroyMerlinCDPRecruitmentApplication application;

  @Test
  public void shouldLoadApplicationContext_whenStartingApplication() {
    //Ensure we are correctly loading the contexts.
    assertNotNull(application);
  }
}
