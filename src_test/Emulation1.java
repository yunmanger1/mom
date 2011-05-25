public class Emulation1 {

   public static void main(String[] args) throws Exception {
      long start = System.currentTimeMillis();
      int N = 100;
      Thread[] rn = new Thread[N / 2];
      int k = 0;
      for (int i = 0; i < N; i += 2) {
         String from = String.format("701%07d", i);
         String to = String.format("701%07d", i + 1);
         EmulationUtils.addBalance(from, 30);
         EmulationUtils.reserveCall(from, to);
//         rn[k++] = new Request(from, to);
      }
//      for (int i = 0; i < k; i++) {
//         rn[i].start();
//      }
//      for (int i = 0; i < k; i++) {
//         rn[i].join();
//      }
      System.out.printf("Took %d ms to add 100 to balance start all sessions", System.currentTimeMillis() - start);
//      for (int i = 0; i < N; i += 2) {
//         String from = String.format("701%07d", i);
//         String to = String.format("701%07d", i + 1);
//         EmulationUtils.overSession(from, to);
//      }
   }

   public static class Request extends Thread implements Runnable {

      private final String from;
      private final String to;

      public Request(String from, String to) {
         this.from = from;
         this.to = to;
      }

      @Override
      public void run() {
         try {
            EmulationUtils.addBalance(from, 30);
            EmulationUtils.reserveCall(from, to);
         } catch (Exception e) {
            e.printStackTrace();
         }
      }

   }
}
