
package redglasss;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

/**
 *
 * @author Balta XD
 */
//--------------------------------------------------------------el timer
class reloj implements Runnable{
    int cuenta, veloz;
    public reloj(int v){
        cuenta=0;
        veloz= v;
       
    }
    @Override
    public void run() {
        try {
            while(true){
                Thread.sleep(veloz);
                cuenta++;
               //System.out.println("Entra a contar");
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(reloj.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int pasados(int ref){ //guardar la cuenta en momento MUCHO CUIDADO AQUÍ
            return cuenta-ref;      //y mandarsela como referencia
    }
}

//-------------------------------------------------------------Simples procesos
class proceso{
    Random r;
    public final String ID;
    public int tiempoT, Q, tRestante;
    
    public proceso(){
        r = new Random();
        ID = Integer.toString(r.nextInt(10000));
        tiempoT = r.nextInt(100)+50;
        Q = r.nextInt(10)+5;
        tRestante = Q;
        System.out.println(ID+" "+tiempoT+" "+Q);
    }
}
//------------------------------------------------------------------Creador de procesos
class creador implements Runnable{
    public ArrayList <proceso> listos;
    reloj conta;
    int ref,inter;
    Random r;
    
    public creador(reloj conta){
        this.conta=conta;
        listos = new  ArrayList<proceso>();
       // conta = new reloj(1000); //Se planea una variable segun algun botón
        r = new Random();
        System.out.println("Entra a run");
        listos.add(new proceso());
       
    }
    @Override
    public void run() {
        while(true){
            
            ref=conta.cuenta;
            inter = r.nextInt(10);
            while(conta.pasados(ref)<inter);//dormir hilo
            listos.add(new proceso());
            System.out.println("Creado nuevo");
        }
    }
}

//--------------------------------------------------------------------Donde ocurre la magia
class procesamiento implements Runnable{
    
     ArrayList <proceso> bloqueados; 
     reloj conta;
     creador listos;
     int ref, bloq=0, Q=0, tRestante=0;
     String listaLis="ID  Restante\n", listaBloq="ID  Restante\n";
     Random r;
     
     public procesamiento() {
         
         bloqueados = new  ArrayList<proceso>();
         bloqueados.add(new proceso());
         System.out.println("Entra reloj");
         conta = new reloj (1000); //mandar contructor valor variable para velocidad 
         listos = new creador(conta);
         r = new Random();
         Thread c = new Thread(conta);
         Thread l = new Thread(listos);
         c.start();
         l.start();
    }

//--------------------------------------------------------------------------------RUN
      @Override
    public void run() {
//validar si hay algo en ambos arreglos antes de mandar ejecutar y sacar de bloqueo
        while(true){
            if(listos.listos.size()>0)
           ejecutando(listos.listos.get(0));
            else
                System.out.println("No hay Listos");
            
                
           
            if(r.nextInt(3)==1 && bloqueados.size()>0){
                bloq=(r.nextInt(bloqueados.size()));
               listos.listos.add(bloqueados.get(bloq));
               System.out.println("Salio de bloqueados "+bloqueados.get(bloq).ID);
               bloqueados.remove(bloq);
               bloqueados.trimToSize();
               imprimirListos();
               imprimirBloq();
               
            }
            
        }   
    }
    
    public void ejecutando (proceso Ejecutar){
        int interrup =0;
        ref=conta.cuenta;
        this.Q=Ejecutar.Q;
        this.tRestante = Ejecutar.tRestante;
        System.out.println("Entro\nID  Total Quantum \n"+Ejecutar.ID+" "+Ejecutar.tiempoT+"   "+Ejecutar.Q);
        while(conta.pasados(ref)<=Ejecutar.Q && Ejecutar.tRestante>0){
            if(Ejecutar.tiempoT>0){
                    Ejecutar.tRestante--;
                    this.tRestante--;
                    Ejecutar.tiempoT--;
                    System.out.println(Ejecutar.tRestante);
                 try {
                     Thread.currentThread().sleep(1000);

                     //System.out.println(ref);
                 } catch (InterruptedException ex) {
                     Logger.getLogger(procesamiento.class.getName()).log(Level.SEVERE, null, ex);
                 }

                 if(r.nextInt(5)==2 && listos.listos.size()>0){ 
                       interrup =1;
                       bloqueadoOListo(interrup);
                       break;
                    }
                  imprimirListos();
                       imprimirBloq();
            }else{
                JOptionPane.showMessageDialog(null, "El proceso "+Ejecutar.ID+" terminó","Proceso Terminado" ,
JOptionPane.INFORMATION_MESSAGE);
            }
        }
        Ejecutar.tRestante = Ejecutar.Q;
        System.out.println("Tiempo agotado. ¡Siguiente!");
        if(interrup==0) 
            bloqueadoOListo(interrup);
    }
    
    public void bloqueadoOListo(int señal){
        if(señal == 1){
             bloqueados.add(listos.listos.get(0));
                System.out.println("Entró bloqueados "+listos.listos.get(0).ID);
               listos.listos.remove(0);
               listos.listos.trimToSize();
        }else{
           listos.listos.add(listos.listos.get(0));
                listos.listos.remove(0);
               listos.listos.trimToSize();
        }
               imprimirListos();
               imprimirBloq();
    }
    public void imprimirListos(){
        listaLis="ID   Restante";
        if(listos.listos.size()>0){
        for(int i=0; i<listos.listos.size(); i++){
            listaLis += "\n"+listos.listos.get(i).ID+"   "+listos.listos.get(i).tiempoT;
        }
        }else
            listaLis +="No hay listos";
         
    }
     public void imprimirBloq(){
         listaBloq="ID   Restante";
        if(bloqueados.size()>0){
        for(int i=0; i<bloqueados.size(); i++){
            listaBloq += "\n"+bloqueados.get(i).ID+"   "+bloqueados.get(i).tiempoT;
        }
        }else
            listaLis +="No hay listos";
    }

   
    
}
public class RedGlassS extends javax.swing.JFrame {

    public static void main(String[] args) {
    }
    
}
