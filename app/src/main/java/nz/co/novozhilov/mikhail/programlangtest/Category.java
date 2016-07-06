package nz.co.novozhilov.mikhail.programlangtest;

/**
 * Category entity
 *
 * @author Mikhail Novozhilov novomic@gmail.com
 */
final class Category {
    private int id;
    private String name;
    private int test_id;
    private boolean enabled;

    public Category(int id, String name, int test_id, boolean enabled) {
        this.id = id;
        this.name = name;
        this.test_id = test_id;
        this.enabled = enabled;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getTestId() {
        return this.test_id;
    }

    public boolean getEnabled() {
        return this.enabled;
    }
}
