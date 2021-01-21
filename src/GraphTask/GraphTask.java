package GraphTask;

/* ALLIKAD:
https://bitbucket.org/itc_algorithms/kt6.git
https://www.geeksforgeeks.org/transitive-closure-of-a-graph/
*/

/** Container class to different classes, that makes the whole
 * set of classes one class formally.
 */
public class GraphTask {

   /**
    * Main method.
    */
   public static void main(String[] args) {
      GraphTask a = new GraphTask();
      a.run();
   }

   /**
    * Actual main method to run examples and everything.
    */
   public void run() {

      // huge directed graph
      long start = System.currentTimeMillis();
      Graph g = new Graph("Huge directed graph");
      g.createRandomTree(2000, false);
      Graph rtc = g.createReflexiveTransitiveClosureGraph();
      System.out.println(System.currentTimeMillis() - start);

   }


   /**
    * Vertex class.
    */
   class Vertex {

      private String id;
      private Vertex next;
      private Arc first;
      private int info = 0;

      Vertex(String s, Vertex v, Arc e) {
         id = s;
         next = v;
         first = e;
      }

      Vertex(String s) {
         this(s, null, null);
      }

      @Override
      public String toString() {
         return id;
      }
   }


   /**
    * Arc represents one arrow in the graph. Two-directional edges are
    * represented by two Arc objects (for both directions).
    */
   class Arc {

      private String id;
      private Vertex target;
      private Arc next;

      Arc(String s, Vertex v, Arc a) {
         id = s;
         target = v;
         next = a;
      }

      Arc(String s) {
         this(s, null, null);
      }

      @Override
      public String toString() {
         return id;
      }
   }


   /**
    * Graph class.
    */
   class Graph {

      private String id;
      private Vertex first;
      private int info = 0;

      Graph(String s, Vertex v) {
         id = s;
         first = v;
      }

      Graph(String s) {
         this(s, null);
      }

      @Override
      public String toString() {
         String nl = System.getProperty("line.separator");
         StringBuffer sb = new StringBuffer(nl);
         sb.append(id);
         sb.append(nl);
         Vertex v = first;
         while (v != null) {
            sb.append(v.toString());
            sb.append(" -->");
            Arc a = v.first;
            while (a != null) {
               sb.append(" ");
               sb.append(a.toString());
               sb.append(" (");
               sb.append(v.toString());
               sb.append("->");
               sb.append(a.target.toString());
               sb.append(")");
               a = a.next;
            }
            sb.append(nl);
            v = v.next;
         }
         return sb.toString();
      }

      public Vertex createVertex(String vid) {
         Vertex res = new Vertex(vid);
         res.next = first;
         first = res;
         return res;
      }

      public Arc createArc(String aid, Vertex from, Vertex to) {
         Arc res = new Arc(aid);
         res.next = from.first;
         from.first = res;
         res.target = to;
         return res;
      }

      /**
       * Create a connected undirected random tree with n vertices.
       * Each new vertex is connected to some random existing vertex.
       *
       * @param n number of vertices added to this graph
       */
      public void createRandomTree(int n, boolean isUndirected) {
         if (n <= 0)
            return;
         Vertex[] varray = new Vertex[n];
         for (int i = 0; i < n; i++) {
            varray[i] = createVertex("v" + (n - i));
            if (i > 0) {
               int vnr = (int) (Math.random() * i);
               createArc("a" + varray[vnr].toString() + "_"
                       + varray[i].toString(), varray[vnr], varray[i]);
               if (isUndirected) {
                  createArc ("a" + varray [i].toString() + "_"
                          + varray [vnr].toString(), varray [i], varray [vnr]);
               }
            }
         }
      }

      /**
       * Create an adjacency matrix of this graph.
       * Side effect: corrupts info fields in the graph
       *
       * @return adjacency matrix
       */
      public int[][] createAdjMatrix() {
         info = 0;
         Vertex v = first;
         while (v != null) {
            v.info = info++;
            v = v.next;
         }
         int[][] res = new int[info][info];
         v = first;
         while (v != null) {
            int i = v.info;
            Arc a = v.first;
            while (a != null) {
               int j = a.target.info;
               res[i][j]++;
               a = a.next;
            }
            v = v.next;
         }
         return res;
      }

      /**
       * Create a connected simple (undirected, no loops, no multiple
       * arcs) random graph with n vertices and m edges.
       *
       * @param n number of vertices
       * @param m number of edges
       */
      public void createRandomSimpleGraph(int n, int m) {
         if (n <= 0)
            return;
         if (n > 2500)
            throw new IllegalArgumentException("Too many vertices: " + n);
         if (m < n - 1 || m > n * (n - 1) / 2)
            throw new IllegalArgumentException
                    ("Impossible number of edges: " + m);
         first = null;
         createRandomTree(n, true);       // n-1 edges created here
         Vertex[] vert = new Vertex[n];
         Vertex v = first;
         int c = 0;
         while (v != null) {
            vert[c++] = v;
            v = v.next;
         }
         int[][] connected = createAdjMatrix();
         int edgeCount = m - n + 1;  // remaining edges
         while (edgeCount > 0) {
            int i = (int) (Math.random() * n);  // random source
            int j = (int) (Math.random() * n);  // random target
            if (i == j)
               continue;  // no loops
            if (connected[i][j] != 0 || connected[j][i] != 0)
               continue;  // no multiple edges
            Vertex vi = vert[i];
            Vertex vj = vert[j];
            createArc("a" + vi.toString() + "_" + vj.toString(), vi, vj);
            connected[i][j] = 1;
            createArc("a" + vj.toString() + "_" + vi.toString(), vj, vi);
            connected[j][i] = 1;
            edgeCount--;  // a new edge happily created
         }
      }

      /**
       * Create reflexive transitive closure matrix of this graph.
       * It is later used to create reflexive transitive closure graph.
       * Side effect: corrupts info fields in the graph
       *
       * @return Matrix that represents reflexive transitive closure of this graph
       */
      public int[][] createReflexiveTransitiveClosureMatrix() {
         info = 0;
         Vertex v = first;
         while (v != null) {
            v.info = info++;
            v = v.next;
         }

         int[][] refTransMatrix = new int[info][info];
         int[][] adjMatrix = createAdjMatrix();

         // copy one matrix to another
         for (int i = 0; i < info; i++)
            for (int j = 0; j < info; j++)
               refTransMatrix[i][j] = adjMatrix[i][j];

         for (int k = 0; k < info; k++) {
            for (int i = 0; i < info; i++) {
               for (int j = 0; j < info; j++) {
                  refTransMatrix[i][j] = (refTransMatrix[i][j] != 0) || ((refTransMatrix[i][k] != 0) && (refTransMatrix[k][j] != 0)) || (i == j) ? 1 : 0;
               }
            }
         }
         return refTransMatrix;
      }

      /**
       * Create reflexive transitive closure of this graph.
       * @return New graph object that is reflexive transitive closure of this graph
       */
      public Graph createReflexiveTransitiveClosureGraph() {
         int[][] matrix = createReflexiveTransitiveClosureMatrix();
         Graph reflexiveTransitiveClosureGraph = new Graph("Reflexive transitive closure of graph: " + this.id);

         Vertex[] varray = new Vertex[matrix.length];
         for (int i = matrix.length-1; i >= 0; i--) {
            varray[i] = reflexiveTransitiveClosureGraph.createVertex("v" + (i + 1));
         }

         for (int i = matrix.length-1; i >= 0; i--) {
            for (int j = matrix.length-1; j >= 0; j--) {
               if (matrix[i][j] == 1) {
                  reflexiveTransitiveClosureGraph.createArc("a" + varray[i].toString() + "_" + varray[j].toString(), varray[i], varray[j]);
               }
            }
         }
         return reflexiveTransitiveClosureGraph;
      }
   }
}
