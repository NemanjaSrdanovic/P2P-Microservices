package parsers;
 
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
 
public class Marshaller {
    private byte[] sendBuff;
    private ByteArrayOutputStream bs;
    private ByteArrayInputStream bis;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Object object;
    private InetAddress ipHost;
   
    public Marshaller() {}
       
    public Marshaller(InetAddress address) {
        this.ipHost = address;
    }
   
    public synchronized DatagramPacket makeDatagramPacket(Object obj, byte[] buffer, String ipPort) {
        try {
            bs = new ByteArrayOutputStream(buffer.length);
            oos = new ObjectOutputStream(bs);
           
           // oos.flush();
            oos.writeObject(obj);
            oos.flush();
           
            byte[] sendBuf = bs.toByteArray();
            
            oos.close();
            bs.close();
           
            return new DatagramPacket(sendBuf, sendBuf.length, InetAddress.getByName(ipPort.split(":")[0])
                    , Integer.parseUnsignedInt(ipPort.split(":")[1]));
        } catch (IOException e) {
            System.out.println("Error Marshaller Making DataPacket <---------------");
            e.printStackTrace();
        }finally {
           
        }
       
        return null;
    }
   
    public synchronized DatagramPacket makeDatagramPacketFrom(Object obj, byte[] buffer, int portHost) throws IOException {
        try {
            bs = new ByteArrayOutputStream(buffer.length);
            oos = new ObjectOutputStream(bs);
           
           // oos.flush();
            oos.writeObject(obj);
            oos.flush();
           
            byte[] sendBuf = bs.toByteArray();
            
            oos.close();
            bs.close();
           
           
            return new DatagramPacket(sendBuf, sendBuf.length, ipHost, portHost);
        } catch (IOException e) {
            System.out.println("Error Marshaller Making DataPacket <---------------");
            e.printStackTrace();
        }finally {
 
        }
       
        return null;
    }
 
    public synchronized Object makeObjectFrom(DatagramPacket packet) {
      	
    	
        try {
            bis = new ByteArrayInputStream(packet.getData());
            ois = new ObjectInputStream(/*new BufferedInputStream(bis)*/ bis);
            object = (Object) ois.readObject();
            ois.close();
           
            return object; 
        } catch (IOException e) {
            System.out.println("Error Marsahller IO <----------------");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Error Marshaller CLASS <-----------------");
            e.printStackTrace();   
        }
       
        return null;   
    }
   
   
    /*Transforms object to byte to be stored into an database
     * together with the rest of the message informations*/
    public synchronized byte[] transformObjectToByte(Object obj) {
 
        byte[] byteArrayObject = null;
        try {
           
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
           
     
            byteArrayObject = bos.toByteArray();
            
            oos.close();
            bos.close();
            
        } catch (Exception e) {
            System.out.println("Error Marshaller Making Byte <---------------");
            e.printStackTrace();
            return byteArrayObject;
        }
        return byteArrayObject;
       
    }
   
    public synchronized Object transformByteToObject(byte[] byteInput) {
 
        Object obj = null;
        ByteArrayInputStream bais;
        ObjectInputStream ins;
       
        try {
       
        bais = new ByteArrayInputStream(byteInput);
       
        ins = new ObjectInputStream(bais);
         obj =(Object)ins.readObject();
       
        ins.close();
   
        }
        catch (Exception e) {
            System.out.println("Error Marshaller Making Object <---------------");
            e.printStackTrace();
        }
        return obj;
    }  
}