
package redglasss;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

//------------------------------------------------------------------------------RELOJ---------------------------------------------------------------------
class reloj implements Runnable{
    int cuenta, veloz;
    public reloj(int v){
        cuenta=0;
        veloz= v;
        run();
    }
    @Override
    public void run() {
        try {
            while(true){
                Thread.sleep(veloz);
                cuenta++;
                //System.out.println(cuenta);
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
    }
}
//------------------------------------------------------------------Creador de procesos
class creador implements Runnable{
    public ArrayList <proceso> listos;
    reloj conta;
    int ref,inter;
    Random r;
    
    public creador(){
        listos = new  ArrayList<proceso>();
        conta = new reloj(1000); //Se planea una variable segun algun botón
        r = new Random();
        listos.add(new proceso());
    }
    @Override
    public void run() {
        ref=conta.cuenta;
        inter = r.nextInt(7);
        while(conta.pasados(ref)<inter);//dormir hilo
        listos.add(new proceso());
    }
}

//--------------------------------------------------------------------Donde ocurre la magia
class procesamiento implements Runnable{
    
     ArrayList <proceso> bloqueados; 
     reloj conta;
     creador listos;
     int ref, bloq;
     Random r;
     
     public procesamiento() {
         bloqueados = new  ArrayList<proceso>();
         listos = new creador();
         conta = new reloj (1000); //mandar contructor valor variable para velocidad 
         r = new Random();
    }

//--------------------------------------------------------------------------------RUN
    @Override
    public void run() {
//validar si hay algo en ambos arreglos antes de mandar ejecutar y sacar de bloqueo
        while(true){
            ejecutando(listos.listos.get(0));
            if(r.nextInt(5)==2){
               bloqueados.add(listos.listos.get(0));
               listos.listos.remove(0);
               listos.listos.trimToSize();
            }
            if(r.nextInt(3)==2){
                bloq=r.nextInt(bloqueados.size())-1;
               listos.listos.add(bloqueados.get(bloq));
               bloqueados.remove(bloq);
               bloqueados.trimToSize();
            }
        }   
    }
    
    public void ejecutando (proceso Ejecutar){
        ref=conta.cuenta;
        while(conta.pasados(ref)<=Ejecutar.Q)
            System.out.println(Ejecutar.tiempoT--);
        
        
    }
}

public class RedGlassS {

    public static void main(String[] args) {
        JOptionPane.showMessageDialog(null, "I'm Batman");
    }
    
}
