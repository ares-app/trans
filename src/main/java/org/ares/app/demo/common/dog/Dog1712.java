package org.ares.app.demo.common.dog;


import SuperDog.*;

public class Dog1712{
 
    @SuppressWarnings("unused")
	public static boolean hasDog(){
    	String vendorCode = new String("H3RzJq4fN2fj98zIDNJi97FnDr+8GchqN6RRa"
    			+ "4l9fsE8XXUgCfyj25dQL9qKAmdUyyrRL8vedS6QWyG8hLzZDJ62tBSBIrk"
    			+ "RDYaG9RwtyU6WODZRq4CiRdZLfIrsSrJI2dJID5oAQManeLGRZZMPc70WG"
    			+ "q6SKSbmCs2483N4Ga/ZVc4Ud66nEhS+lhQkexb/kzOFI+OmzVlImR0O+4r"
    			+ "E6VfAaj+ql1Ey4bYarat2XSZ2FnzdMxWeqqTl2KKmrFMpXP4ojJmlXV+/v"
    			+ "et0Sm6xLQOl7hU5Pdah2x7i2Xxmw3jlTzMUGjeF+fBlcPpj3UCDvfPKxXE"
    			+ "wD47WCJmn2q8J+NxAIZnMu7vJOjjKQzGmTQ0hNVJCf/9WApUyHYtTdquv+"
    			+ "ePNypn9BK05hNXcq++/oeY+mjBiQPQjOF2xkeutPSvnZe9Lbmm5CXdHGKP"
    			+ "/KbmNEUzp25mwyywPLiRvGs1le23uszuP3+Gsmox4WRhldd/57DbhVR28o"
    			+ "VfXxUY84vrL4iNvOMlQKrfTtwhqR7df1wPMMAWNRUQy7tmjCWxFBgyBxf4"
    			+ "hh7itCzZhZlkxCCN/V8lGcRNJB5jamG6JYCGmuZE616LG0LJnabGx7EUnw"
    			+ "Em8lBCQ3wJj0hv38DFHjRMtHZpZzf3vyoO/rdbWI4jlsbdnXkxZCWFPq7N"
    			+ "ikjDib+H35I7GQ8Z6D3e1UnO8Pju0QGI1Gvrb4QeQ/zTRsIzkymg3/xaLk"
    			+ "YvDzSm27NvfJ9fVfWLIdB453kVS/A7ENO0fyOWBjOX9w1xIgH0QU8GtToW"
    			+ "GP+Jxuyecbut3RDqYfCN9ghWMMvcBhYsgzj2G3QOVB6uUY9Rx/LKBjJSdu"
    			+ "7Vvf7EQbgIS+yVE7vCjDCIYWYjRE56/V0cXqj+g9bh/xMfzKOIjIK2lfBR"
    			+ "GHxqMFP/uJ9S5njOQ1qJl1gyvzGx8eTLbzICFhCVxHDLAxVO8ii7GECzAj"
    			+ "INyoC/ksor5uDm9CQ==");
    	String strdata = "in3Kghhgygx4wWdkKtCPaQ";
    	boolean result=false;
        int status;
        String info;
        String strEncrypted;
        Dog curDog = new Dog(Dog.DOG_DEFAULT_FID);
        curDog.login(vendorCode);
        status = curDog.getLastError();
        switch (status)
        {
            case DogStatus.DOG_STATUS_OK:
                //System.out.println("OK");
                break;
            case DogStatus.DOG_FEATURE_NOT_FOUND:
                System.out.println("no SuperDog DEMOMA key found");
                break;
            case DogStatus.DOG_NOT_FOUND:
                System.out.println("SuperDog not found");
                break;
            case DogStatus.DOG_INV_VCODE:
                System.out.println("invalid vendor code");
                break;
            case DogStatus.DOG_LOCAL_COMM_ERR:
                System.out.println("communication error between API and local SuperDog License Manager");
                break;
            default:
                System.out.println("login to default feature failed");
        }


       // System.out.print("\nget session info                 : ");

        info = curDog.getSessionInfo(Dog.DOG_KEYINFO);
        status = curDog.getLastError();

        switch (status)
        {
            case DogStatus.DOG_STATUS_OK:
                //System.out.print("OK, SuperDog attributes retrieved\n\n"
                 //                + "SuperDog info:\n===============\n" + info
                 //                + "\n===============\n");
                break;
            case DogStatus.DOG_INV_HND:
                System.out.println("handle not active");
                break;
            case DogStatus.DOG_INV_FORMAT:
                System.out.println("unrecognized format");
                break;
            case DogStatus.DOG_NOT_FOUND:
                System.out.println("SuperDog not found");
                break;
            case DogStatus.DOG_LOCAL_COMM_ERR:
                System.out.println("communication error between API and local SuperDog License Manager");
                break;
            default:
                System.out.println("dog_get_sessioninfo failed");
        }
        byte[] membuffer = new byte[32];

        curDog.read(1, 0, membuffer);
        status = curDog.getLastError();
        switch (status)
        {
            case DogStatus.DOG_STATUS_OK:
                //System.out.println("OK");
                //System.out.println(dump(membuffer, "     "));
                break;
            case DogStatus.DOG_INV_HND:
                System.out.println("handle not active");
                break;
            case DogStatus.DOG_INV_FILEID:
                System.out.println("invalid file id");
                break;
            case DogStatus.DOG_MEM_RANGE:
                System.out.println("beyond memory range of attached SuperDog");
                break;
            case DogStatus.DOG_NOT_FOUND:
                System.out.println("SuperDog not found");
                break;
            case DogStatus.DOG_LOCAL_COMM_ERR:
                System.out.println("communication error between API and local SuperDog License Manager");
                break;
            default:
                System.out.println("read data file failed\n");
        }
        String readString = dump(membuffer);
  
        
        strEncrypted = curDog.encryptString(strdata);
        status = curDog.getLastError();

        switch (status)
        {
            case DogStatus.DOG_STATUS_OK:
                //System.out.println("encryption ok");
                break;
            case DogStatus.DOG_INV_HND:
                System.out.println("handle not active");
                break;
            case DogStatus.DOG_TOO_SHORT:
                System.out.println("data length too short");
                break;
         
            case DogStatus.DOG_FEATURE_NOT_FOUND:
                System.out.println("no SuperDog DEMOMA key found");
                break;
            default:
                System.out.println("encryption failed");
        }
//        System.out.println("redading    :"+readString);
//        System.out.println("strEncrypted:"+strEncrypted);
        if(readString.equals(strEncrypted)){
        	result = true;
        }
        curDog.logout(); 
        return result;


    }
    private static String dump(byte[] data)
    {
        int i, j;
        byte b;
        byte[] s = new byte[32];
        byte hex[] = {0};
        if (data.length == 0) return "";
        s[0] = 0;
        j = 0;
        for (i = 0; i < data.length; i++) 
        {
            b = data[i];
            if ((b < 32) || (b > 127)) s[j] = '.'; else s[j] = b;
            if (j < 15)
                s[j+1] = 0;
            hex[0] = b; 
            j++;
        }
        return new String(s);
    }  
    
    public static void main(String argv[]) throws java.io.IOException
    {
    	if(hasDog()){
    		System.out.println("success!!!");
    	}
    }   
 
}
