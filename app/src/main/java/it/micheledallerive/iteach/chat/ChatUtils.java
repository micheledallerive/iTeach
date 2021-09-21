package it.micheledallerive.iteach.chat;

public class ChatUtils {

    public static String getChatId(String user1, String user2){
        for(int i=0;i<user1.length();i++){
            int c1 = user1.charAt(i);
            int c2 = user2.charAt(i);
            if(c1<c2)
                return user1+user2;
            if(c2<c1)
                return user2+user1;
        }
        return null;
    }

}
