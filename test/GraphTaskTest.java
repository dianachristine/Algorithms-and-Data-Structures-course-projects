import GraphTask.GraphTask;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/** Testklass.
 * @author jaanus
 */
public class GraphTaskTest {

   @Test(timeout=20000)
   public void test1() { 
      GraphTask.main (null);
      assertTrue ("There are no tests", true);
   }

}

