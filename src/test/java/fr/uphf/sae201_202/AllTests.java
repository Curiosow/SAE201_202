package fr.uphf.sae201_202;

public class AllTests {
    public static void main(String[] args) {
        int totalTests = 0;
        int testsFailed = 0;

        // ElementTest
        try {
            ElementTest elementTest = new ElementTest();
            elementTest.testDefaultConstructor();
            elementTest.testParameterizedConstructor();
            elementTest.testSettersAndGetters();
            elementTest.testGetImgLink();
            elementTest.testToString();
            totalTests += 5;
        } catch (Exception e) {
            testsFailed++;
            e.printStackTrace();
        }

        // GridTest
        try {
            GridTest gridTest = new GridTest();
            gridTest.testConstructor();
            gridTest.testToString();
            totalTests += 2;
        } catch (Exception e) {
            testsFailed++;
            e.printStackTrace();
        }

        // MapTest
        try {
            MapTest mapTest = new MapTest();
            mapTest.testConstructor();
            mapTest.testAddMine();
            totalTests += 2;
        } catch (Exception e) {
            testsFailed++;
            e.printStackTrace();
        }

        // MineTest
        try {
            MineTest mineTest = new MineTest();
            mineTest.testConstructor();
            mineTest.testSetAndGetOres();
            mineTest.testSetAndGetOreLess();
            mineTest.testSetRandomAmount();
            mineTest.testIdAssignment();
            totalTests += 5;
        } catch (Exception e) {
            testsFailed++;
            e.printStackTrace();
        }

        // StorageTest
        try {
            StorageTest storageTest = new StorageTest();
            storageTest.testConstructor();
            storageTest.testSetAndGetCapacity();
            storageTest.testGetImgLink();
            storageTest.testToString();
            totalTests += 4;
        } catch (Exception e) {
            testsFailed++;
            e.printStackTrace();
        }

        // Print summary
        System.out.println("Total tests run: " + totalTests);
        System.out.println("Tests failed: " + testsFailed);
    }
}
