package es.uclm.iso2.sescam;

public enum Priority {
    URGENT(1),
    PREFERENTE(2),
    ORDINARIO(3);

    private final int order;

    Priority(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }
}
