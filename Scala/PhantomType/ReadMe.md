# Phantom Type

Phantom Type 은 compiler 가 정적으로 type 을 check 하기 위해서 사용하지만 
runtime 에 영향을 끼치지는 않는다. 

> A phantom type is a manifestation of abstract type that has no effect 
on the runtime. These are useful to prove static properties of the code 
using type evidences. As they have no effect on the runtime they can 
be erased from the resulting code by the compiler once it has shown the 
constraints hold.
