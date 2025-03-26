package com.smb116.tp6thread;

public abstract class HumiditySensorAbstract{
    // valeur du capteur, prÃ©cision de 0.1
    public abstract float value() throws Exception;

    // pÃ©riode minimale entre deux lectures
    public abstract long minimalPeriod();
}
