import java.util.*;
 
//classe da estrutura de dados (recurso) compartilhado entre as threads
class Vetor {
  //recurso compartilhado
  private List<Integer> r = new ArrayList<Integer>();
  //construtor
  public void start(int tam){

     System.out.println(this.r);
     for(int i=0; i<tam;i++){
        this.r.add(1);
     }
  }

  //operacao de escrita sobre o recurso compartilhado
  /*public void inc() { 
     this.r++; 
  }*/
  //operacao de leitura sobre o recurso compartilhado
  
  /*public int get() { 
     return this.r; 
  }
  */
  
  // ou...
  public synchronized int tamanho() { 
      return this.r.size();
  }

  public synchronized void alterar(int i, int valor) { 
     this.r.set(i, valor); 
  }

  public synchronized int get(int i) { 
      return this.r.get(i); 
  }

  public synchronized void printget() { 
     String response = "[";
      for (int i=0; i<this.r.size(); i++) {
         response += this.r.get(i);
         if(i<this.r.size()-1){
            response+=", ";
         }
      }
      
      response += "]";
      System.out.println(response); 
  }
  
}
class Global {
  //recurso compartilhado
  private int global;
  //construtor
  public Global() { 
     this.global = 0; 
  }

  //operacao de escrita sobre o recurso compartilhado
  /*public void inc() { 
     this.r++; 
  }*/
  //operacao de leitura sobre o recurso compartilhado
  
  /*public int get() { 
     return this.r; 
  }
  */
  
  // ou...

  public synchronized void inc() { 
     this.global++; 
  }

  public synchronized int get() { 
      return this.global; 
  }
  
}

//classe que estende Thread e implementa a tarefa de cada thread do programa 
class T extends Thread {
   //identificador da thread
   private int id;
   //objeto compartilhado com outras threads
   Vetor a;
   Vetor b;
   Vetor c;
   Global g;
   
   //construtor
   public T(int tid, Vetor a, Vetor b, Vetor c, Global g) { 
      this.id = tid; 
      this.a = a;
      this.b = b;
      this.c = c;
      this.g = g;
   }

   //metodo main da thread
   public void run() {
      System.out.println("Thread " + this.id + " iniciou!");
      System.out.println("Valor de g " + g.get() );
      
      while(g.get() < a.tamanho()) {
         int global;
         global = g.get();
         g.inc();
         this.c.alterar(global, a.get(global)+b.get(global));
      }
      System.out.println("Thread " + this.id + " terminou!"); 
   }
}

//classe da aplicacao
class SomaVetor {
   static final int N = 3;

   public static void main (String[] args) {
      //reserva espaço para um vetor de threads
      Thread[] threads = new Thread[N];

      //cria uma instancia do recurso compartilhado entre as threads
      Vetor a = new Vetor();
      Vetor b = new Vetor();
      Vetor c = new Vetor();
      Global g = new Global();
      a.start(100);
      b.start(100);
      c.start(100);
      

      System.out.println("a = "); 
      a.printget();
      System.out.println("b = "); 
      b.printget();
      //cria as threads da aplicacao
      for (int i=0; i<threads.length; i++) {
         threads[i] = new T(i, a, b, c, g);
      }

      //inicia as threads
      for (int i=0; i<threads.length; i++) {
         threads[i].start();
      }

      //espera pelo termino de todas as threads
      for (int i=0; i<threads.length; i++) {
         try { threads[i].join(); } catch (InterruptedException e) { return; }
      }

      
      System.out.println("c = "); 
      c.printget();

   }
}
