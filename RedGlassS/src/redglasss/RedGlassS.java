
package redglasss;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    public int tiempoT, Q;
    
    public proceso(){
        r = new Random();
        ID = Integer.toString(r.nextInt(10000));
        tiempoT = r.nextInt(100)+50;
        Q = r.nextInt(10)+5;
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
            inter = r.nextInt(7);
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
     int ref, bloq=0;
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
            ejecutando(listos.listos.get(0));
            if(r.nextInt(5)>2 && listos.listos.size()>0){
               bloqueados.add(listos.listos.get(0));
                System.out.println("Entró bloqueados "+listos.listos.get(0).ID);
               listos.listos.remove(0);
               listos.listos.trimToSize();
            }else{
                listos.listos.add(listos.listos.get(0));
                listos.listos.remove(0);
               listos.listos.trimToSize();
            }
            if(r.nextInt(3)>1 && bloqueados.size()>0){
                bloq=(r.nextInt(bloqueados.size()));
               listos.listos.add(bloqueados.get(bloq));
               System.out.println("Salio de bloqueados "+bloqueados.get(bloq).ID);
               bloqueados.remove(bloq);
               bloqueados.trimToSize();
               
            }
        }   
    }
    
    public void ejecutando (proceso Ejecutar){
        ref=conta.cuenta;
        System.out.println("Entro\nID  Total Quantum \n"+Ejecutar.ID+" "+Ejecutar.tiempoT+"   "+Ejecutar.Q);
        while(conta.pasados(ref)<=Ejecutar.Q && Ejecutar.Q>0){
            System.out.println(Ejecutar.tiempoT--);
         try {
             Thread.currentThread().sleep(1000);
             //System.out.println(ref);
         } catch (InterruptedException ex) {
             Logger.getLogger(procesamiento.class.getName()).log(Level.SEVERE, null, ex);
         }
        }
        System.out.println("Tiempo agotado. ¡Siguiente!");        
    }
}

public class RedGlassS {

    public static void main(String[] args) {
        JOptionPane.showMessageDialog(null, "I'm Batman");
        procesamiento nuevo = new procesamiento();
        nuevo.run();
    }
    
}
