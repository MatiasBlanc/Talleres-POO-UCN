package problema2.domain;

import problema2.domain.Dinosaurios.*;

public interface Visitor {
    void visit(Tiranosaurio t);
    void visit(Triceratops tr);
    void visit(Velociraptor v);
    void visit(Brachiosaurus b);
}