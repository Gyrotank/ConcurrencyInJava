package basicthreadsandrunnables;

//Enum singleton by Joshua Bloch
//PROS: no multi-threading problems
//CONS: eager initialization
//If you wonder how this works, imagine it compiled into something like:
/*
public final class SingletonV6 {
    public final static SingletonV6 INSTANCE = new SingletonV6(); 
}
 */ 
public enum SingletonV6 {
    INSTANCE;
    
    public static SingletonV6 getInstance() {
        return INSTANCE;
    }
}
