package com.vaadin.addon.modeltable;

import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import com.vaadin.util.CurrentInstance;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Field;

/**
 * Created by basakpie on 2017-05-18.
 */
@RunWith(MockitoJUnitRunner.class)
public class TestModelTable extends ModelTable<TestItem> {

	private static final long serialVersionUID = 1L;

	UI ui;

    @Mock
    Page page;

    @Mock
    VaadinSession vaadinSession;

    @Mock
    VaadinRequest req;

    public TestModelTable() {
        super(TestItem.class);
        setItem(new TestItem());
        setVisibleColumns("field01", "field02", "field03", "field04", "field05", "field07", "field08", "field14");
        setColumnHeaders("필드01", "필드02", "필드03", "필드04", "필드05", "필드07", "필드08", "필드14");
    }

    @Before
    public void setup() throws Exception {
        initUi();
        mockPage();
        mockSession();

    }

    private void initUi() {
        ui = new TestUI();
        CurrentInstance.set(UI.class, ui);
    }

    private void mockPage() throws NoSuchFieldException, IllegalAccessException {
        Field pageField = UI.class.getDeclaredField("page");
        pageField.setAccessible(true);
        pageField.set(ui, page);
    }

    private void mockSession() throws NoSuchFieldException, IllegalAccessException {
        Field sessionField = UI.class.getDeclaredField("session");
        sessionField.setAccessible(true);
        sessionField.set(ui, vaadinSession);
    }


    @Test
    public void testVisibleColumns() {
        setItemDirection(Direction.RIGHT, 3);
        refreshRendered();

        final Object[] rightVisibleColumns = targetTable().getVisibleColumns();
        Assert.assertTrue("right size", 6 == rightVisibleColumns.length);

        setItemDirection(Direction.BOTTOM, 3);
        refreshRendered();

        final Object[] bottomVisibleColumns = targetTable().getVisibleColumns();
        Assert.assertTrue("bottom size", 6 == bottomVisibleColumns.length);
    }

    @Test
    public void testRightDirectionColumnValues() {
        int size = 3;
        setItemDirection(Direction.RIGHT, size);
        refreshRendered();

        TestItem item = (TestItem) sourceTable().firstItemId();

        print(3, 3);

        Assert.assertEquals("필드01 KEY: ", "필드01", (String) targetTable().getContainerProperty(1, "key_0").getValue());
        Assert.assertEquals("필드02 KEY: ", "필드02", (String) targetTable().getContainerProperty(1, "key_1").getValue());
        Assert.assertEquals("필드03 KEY: ", "필드03", (String) targetTable().getContainerProperty(1, "key_2").getValue());
        Assert.assertEquals("필드04 KEY: ", "필드04", (String) targetTable().getContainerProperty(2, "key_0").getValue());
        Assert.assertEquals("필드05 KEY: ", "필드05", (String) targetTable().getContainerProperty(2, "key_1").getValue());
        Assert.assertEquals("필드07 KEY: ", "필드07", (String) targetTable().getContainerProperty(2, "key_2").getValue());
        Assert.assertEquals("필드08 KEY: ", "필드08", (String) targetTable().getContainerProperty(3, "key_0").getValue());
        Assert.assertEquals("필드14 KEY: ", "필드14", (String) targetTable().getContainerProperty(3, "key_1").getValue());
        Assert.assertTrue("NULL KEY: ", targetTable().getContainerProperty(3, "key_2").getValue()==null);

        Assert.assertEquals("필드01 VALUE: ", String.valueOf(item.getField01()), (String) targetTable().getContainerProperty(1, "value_0").getValue());
        Assert.assertEquals("필드02 VALUE: ", String.valueOf(item.getField02()), (String) targetTable().getContainerProperty(1, "value_1").getValue());
        Assert.assertEquals("필드03 VALUE: ", String.valueOf(item.getField03()), (String) targetTable().getContainerProperty(1, "value_2").getValue());
        Assert.assertEquals("필드04 VALUE: ", String.valueOf(item.getField04()), (String) targetTable().getContainerProperty(2, "value_0").getValue());
        Assert.assertEquals("필드05 VALUE: ", String.valueOf(item.getField05()), (String) targetTable().getContainerProperty(2, "value_1").getValue());
        Assert.assertEquals("필드07 VALUE: ", String.valueOf(item.isField07()), (String) targetTable().getContainerProperty(2, "value_2").getValue());
        Assert.assertEquals("필드08 VALUE: ", String.valueOf(item.getField08()), (String) targetTable().getContainerProperty(3, "value_0").getValue());
        Assert.assertEquals("필드14 VALUE: ", String.valueOf(item.getField14()), (String) targetTable().getContainerProperty(3, "value_1").getValue());
        Assert.assertTrue("NULL VALUE: ", targetTable().getContainerProperty(3, "value_2").getValue()==null);
    }



    @Test
    public void testBottomDirectionColumnValues() {
        int size = 3;
        setItemDirection(Direction.BOTTOM, size);
        refreshRendered();

        TestItem item = (TestItem) sourceTable().firstItemId();

        print(3, 3);

        Assert.assertEquals("필드01 KEY: ", "필드01", (String) targetTable().getContainerProperty(1, "key_0").getValue());
        Assert.assertEquals("필드04 KEY: ", "필드04", (String) targetTable().getContainerProperty(1, "key_1").getValue());
        Assert.assertEquals("필드08 KEY: ", "필드08", (String) targetTable().getContainerProperty(1, "key_2").getValue());
        Assert.assertEquals("필드02 KEY: ", "필드02", (String) targetTable().getContainerProperty(2, "key_0").getValue());
        Assert.assertEquals("필드05 KEY: ", "필드05", (String) targetTable().getContainerProperty(2, "key_1").getValue());
        Assert.assertEquals("필드14 KEY: ", "필드14", (String) targetTable().getContainerProperty(2, "key_2").getValue());
        Assert.assertEquals("필드03 KEY: ", "필드03", (String) targetTable().getContainerProperty(3, "key_0").getValue());
        Assert.assertEquals("필드07 KEY: ", "필드07", (String) targetTable().getContainerProperty(3, "key_1").getValue());
        Assert.assertTrue("NULL KEY: ", targetTable().getContainerProperty(3, "key_2").getValue()==null);

        Assert.assertEquals("필드01 VALUE: ", String.valueOf(item.getField01()), (String) targetTable().getContainerProperty(1, "value_0").getValue());
        Assert.assertEquals("필드04 VALUE: ", String.valueOf(item.getField04()), (String) targetTable().getContainerProperty(1, "value_1").getValue());
        Assert.assertEquals("필드08 VALUE: ", String.valueOf(item.getField08()), (String) targetTable().getContainerProperty(1, "value_2").getValue());
        Assert.assertEquals("필드02 VALUE: ", String.valueOf(item.getField02()), (String) targetTable().getContainerProperty(2, "value_0").getValue());
        Assert.assertEquals("필드05 VALUE: ", String.valueOf(item.getField05()), (String) targetTable().getContainerProperty(2, "value_1").getValue());
        Assert.assertEquals("필드14 VALUE: ", String.valueOf(item.getField14()), (String) targetTable().getContainerProperty(2, "value_2").getValue());
        Assert.assertEquals("필드03 VALUE: ", String.valueOf(item.getField03()), (String) targetTable().getContainerProperty(3, "value_0").getValue());
        Assert.assertEquals("필드07 VALUE: ", String.valueOf(item.isField07()), (String) targetTable().getContainerProperty(3, "value_1").getValue());
        Assert.assertTrue("NULL VALUE: ", targetTable().getContainerProperty(3, "value_2").getValue()==null);

    }

    private void print(int rowSize, int columnSize) {
        System.out.println("");
        for(int i=1; i <= rowSize; i++) {
            for(int j=0; j < columnSize; j++) {
                String key = (String) targetTable().getContainerProperty(i, "key_"+j).getValue();
                String value = (String) targetTable().getContainerProperty(i, "value_"+j).getValue();
                System.out.print(key + " : ");
                System.out.print(value);
                System.out.print(" | ");
            }
            System.out.println("");
        }
    }



}
