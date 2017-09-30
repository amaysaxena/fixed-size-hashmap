/**
 * Created by amaysaxena on 30/09/2017.
 */
import org.junit.Test;
import org.junit.Assert;

public class FixedSizeHashMapTest {

    @Test
    public void TestSetAndGet() {
        FixedSizeHashmap<String> map = new FixedSizeHashmap<>(1000);
        for(int i = 0; i < 500; i++) {
            String key = "key" + i;
            String value = "value" + i;
            map.set(key, value);
        }

        for(int i = 0; i < 500; i++) {
            String key = "key" + i;
            String value = "value" + i;
            Assert.assertEquals(value, map.get(key));
        }
    }

    @Test
    public void TestSetAndUpdate() {
        FixedSizeHashmap<String> map = new FixedSizeHashmap<>(1000);
        for(int i = 0; i < 500; i++) {
            String key = "key" + i;
            String value = "value" + i;
            map.set(key, value);
        }

        for(int i = 250; i < 500; i++) {
            String key = "key" + i;
            String value = "newValue" + i;
            map.set(key, value);
        }

        for(int i = 0; i < 250; i++) {
            Assert.assertEquals("value" + i, map.get("key" + i));
        }

        for(int i = 250; i < 500; i++) {
            Assert.assertEquals("newValue" + i, map.get("key" + i));
        }

    }

    @Test
    public void TestCapacityEnforcement() {
        FixedSizeHashmap<String> map = new FixedSizeHashmap<>(1000);
        for(int i = 0; i < 1000; i++) {
            String key = "key" + i;
            String value = "value" + i;
            map.set(key, value);
        }

        Assert.assertFalse(map.set("tooManyKeys", "tooManyValues"));
        Assert.assertEquals(map.get("tooManyKeys"), null);
    }

    @Test
    public void TestDelete() {
        FixedSizeHashmap<String> map = new FixedSizeHashmap<>(1000);
        map.set("key1", "value1");
        map.delete("key1");
        Assert.assertTrue(map.load() == 0.0);

    }

    @Test
    public void TestLoad() {
        FixedSizeHashmap<String> map = new FixedSizeHashmap<>(1000);
        for(int i = 0; i < 1000; i++) {
            String key = "key" + i;
            String value = "value" + i;
            map.set(key, value);
        }
        Assert.assertEquals(1.0, map.load(), 0.00001);

        for(int i = 0; i < 500; i++) {
            String key = "key" + i;
            map.delete(key);
        }
        Assert.assertEquals(0.5, map.load(), 0.00001);

        for(int i = 500; i < 1000; i++) {
            String key = "key" + i;
            map.delete(key);
        }
        Assert.assertEquals(0.0, map.load(), 0.00001);
    }
}
