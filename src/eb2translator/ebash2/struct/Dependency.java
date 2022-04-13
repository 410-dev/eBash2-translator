package eb2translator.ebash2.struct;

import java.util.ArrayList;

public class Dependency extends ArrayList<EBClass>{
    public Dependency() {
        super();
    }

    public Dependency(EBClass... classes) {
        super();
        for (EBClass class1 : classes) {
            add(class1);
        }
    }

    public Dependency(ArrayList<EBClass> classes) {
        super();
        for (EBClass ebClass : classes) {
            add(ebClass);
        }
    }


    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (EBClass ebClass : this) {
            stringBuilder.append(ebClass.toString());
        }

        return stringBuilder.toString();
    }


}
