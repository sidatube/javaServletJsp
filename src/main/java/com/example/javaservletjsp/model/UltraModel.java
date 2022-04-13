package com.example.javaservletjsp.model;

import com.example.javaservletjsp.annotation.Column;
import com.example.javaservletjsp.annotation.Table;
import com.example.javaservletjsp.ulti.Config.ConfigSql;
import com.example.javaservletjsp.ulti.ConnectionHelper;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UltraModel<T> {
    Connection connection;
    final Class<T> type;

    public UltraModel(Class<T> type) {
        this.type = type;
        try {
            connection = ConnectionHelper.createConnection();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public boolean save(T obj) {
        String tableName = "";
        StringBuilder sql_txt = new StringBuilder();
        Class<?> objC = obj.getClass();
        if (!objC.isAnnotationPresent(Table.class)) {
            return false;
        }
        Table table = (Table) objC.getDeclaredAnnotation(Table.class);
        if (table.name().length() > 0) {
            tableName = table.name();
        } else {
            tableName = objC.getSimpleName().toLowerCase() + 's';
        }
        try {
            sql_txt.append(ConfigSql.INSERT);
            sql_txt.append(ConfigSql.SPACE);
            sql_txt.append(tableName);
            sql_txt.append(ConfigSql.SPACE);
            Field[] fields = objC.getDeclaredFields();
            StringBuilder colName = new StringBuilder();
            StringBuilder colValue = new StringBuilder();
            colName.append(ConfigSql.OPEN_BRACKET);
            colValue.append(ConfigSql.OPEN_BRACKET);

            for (Field field : fields
            ) {
                if (!field.isAnnotationPresent(Column.class)) {
                    continue;
                }
//                if (field.getName().contains("id")) {
//                    continue;
//                }
                Column column = field.getDeclaredAnnotation(Column.class);

                if (column.name().length() > 0) {
                    colName.append(column.name());
                } else {
                    colName.append(field.getName());
                }
                field.setAccessible(true);
                if ((column.type().contains("VARCHAR") || column.type().contains("TEXT") || column.type().contains("TIMESTAMP")) && field.get(obj) != null) {
                    colValue.append(ConfigSql.APOSTROPHE);
                    colValue.append((field.get(obj)));
                    colValue.append(ConfigSql.APOSTROPHE);
                } else {
                    colValue.append((field.get(obj)));
                }
                colName.append(ConfigSql.COMMA);
                colValue.append(ConfigSql.COMMA);
            }
            colName.setLength(colName.length() - 1);
            colValue.setLength(colValue.length() - 1);
            colName.append(ConfigSql.CLOSE_BRACKET);
            colValue.append(ConfigSql.CLOSE_BRACKET);
            sql_txt.append(colName);
            sql_txt.append(ConfigSql.SPACE);
            sql_txt.append(ConfigSql.VALUES);
            sql_txt.append(ConfigSql.SPACE);
            sql_txt.append(colValue);
//            String sql_txt = ConfigSql.Sql_Products_Create;
            System.out.println(sql_txt.toString());
            PreparedStatement statement = connection.prepareStatement(sql_txt.toString());
            statement.execute();
            System.out.println("Save success");
            return true;
        } catch (SQLException | IllegalAccessException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }
    public int getCount(){
        int count =0;
        if (!type.isAnnotationPresent(Table.class)) {
            return count;
        }
        StringBuilder sqlCount= new StringBuilder();
        sqlCount.append(ConfigSql.SELECT);
        sqlCount.append(ConfigSql.SPACE);
        sqlCount.append(ConfigSql.COUNT);
        sqlCount.append(ConfigSql.OPEN_BRACKET);
        sqlCount.append(ConfigSql.STAR);
        sqlCount.append(ConfigSql.CLOSE_BRACKET);
        sqlCount.append(ConfigSql.SPACE);
        sqlCount.append(ConfigSql.FROM);
        sqlCount.append(ConfigSql.SPACE);
        Table table = (Table) type.getDeclaredAnnotation(Table.class);
        if (table.name().length() > 0) {
            sqlCount.append(table.name());
        } else {
            sqlCount.append(type.getSimpleName().toLowerCase()).append("s");
        }
        try {
            Statement stt = connection.createStatement();
            ResultSet res =stt.executeQuery(sqlCount.toString());
            if (res.next()){
                count= res.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }
    public List<T> getList() {
        List<T> list = new ArrayList<>();
        try {
            StringBuilder getList = new StringBuilder();
            getList.append(ConfigSql.SELECT_ALL);
            getList.append(ConfigSql.SPACE);
            if (!type.isAnnotationPresent(Table.class)) {
                return list;
            }
            Table table = (Table) type.getDeclaredAnnotation(Table.class);
            if (table.name().length() > 0) {
                getList.append(table.name());
            } else {
                getList.append(type.getSimpleName().toLowerCase()).append("s");
            }
            getList.append(ConfigSql.SPACE);
            getList.append(ConfigSql.WHERE);
            getList.append(ConfigSql.SPACE);
            getList.append(ConfigSql.STATUS);
            getList.append(ConfigSql.EQUAL_SIGN);
            getList.append(1);
            Statement stt = connection.createStatement();
            ResultSet rst = stt.executeQuery(getList.toString());
            while (rst.next()) {
                T t = type.newInstance();
                loadResultSetIntoObject(rst, t);// Point 4
                System.out.println(t);
                list.add(t);
            }
        } catch (SQLException | InstantiationException | IllegalAccessException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }
    public List<T> getAll() {
        List<T> list = new ArrayList<>();
        try {
            StringBuilder getList = new StringBuilder();
            getList.append(ConfigSql.SELECT_ALL);
            getList.append(ConfigSql.SPACE);
            if (!type.isAnnotationPresent(Table.class)) {
                return list;
            }
            Table table = (Table) type.getDeclaredAnnotation(Table.class);
            if (table.name().length() > 0) {
                getList.append(table.name());
            } else {
                getList.append(type.getSimpleName().toLowerCase()).append("s");
            }
            getList.append(ConfigSql.SPACE);
            Statement stt = connection.createStatement();
            ResultSet rst = stt.executeQuery(getList.toString());
            while (rst.next()) {
                T t = type.newInstance();
                loadResultSetIntoObject(rst, t);// Point 4
                System.out.println(t);
                list.add(t);
            }
        } catch (SQLException | InstantiationException | IllegalAccessException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }

    public T findById(int id) {
        T obj = null;
        try {
            StringBuilder getObj = new StringBuilder();
            getObj.append(ConfigSql.SELECT_ALL);
            getObj.append(ConfigSql.SPACE);
            if (!type.isAnnotationPresent(Table.class)) {
                return null;
            }
            Table table = (Table) type.getDeclaredAnnotation(Table.class);
            if (table.name().length() > 0) {
                getObj.append(table.name());
            } else {
                getObj.append(type.getSimpleName().toLowerCase()).append("s");
            }
            getObj.append(ConfigSql.SPACE);
            getObj.append(ConfigSql.WHERE);
            getObj.append(ConfigSql.SPACE);
            getObj.append(ConfigSql.ID);
            getObj.append(ConfigSql.EQUAL_SIGN);
            getObj.append(id);
            PreparedStatement stt = connection.prepareStatement(getObj.toString());
            ResultSet rst = stt.executeQuery();
            while (rst.next()) {
                T t = type.newInstance();
                loadResultSetIntoObject(rst, t);// Point 4
                obj = t;
            }
        } catch (SQLException | InstantiationException | IllegalAccessException throwables) {
            throwables.printStackTrace();
        }
        return obj;
    }
    public T findByColumns(String columnName,T params) {
        T obj = null;
        try {
            StringBuilder getObj = new StringBuilder();
            getObj.append(ConfigSql.SELECT_ALL);
            getObj.append(ConfigSql.SPACE);
            if (!type.isAnnotationPresent(Table.class)) {
                return null;
            }
            Field objField =null;
            for (Field field: type.getDeclaredFields()
                 ) {
                if (field.getName().equals(columnName)){

                    objField=field;
                }
            }
            if (objField==null){
                return null;
            }
            Table table = (Table) type.getDeclaredAnnotation(Table.class);
            if (table.name().length() > 0) {
                getObj.append(table.name());
            } else {
                getObj.append(type.getSimpleName().toLowerCase()).append("s");
            }
            Column column = objField.getDeclaredAnnotation(Column.class);

            objField.setAccessible(true);
            getObj.append(ConfigSql.SPACE);
            getObj.append(ConfigSql.WHERE);
            getObj.append(ConfigSql.SPACE);
            getObj.append(columnName);
            getObj.append(ConfigSql.EQUAL_SIGN);
//            getObj.append(objField.get(params));
            if ((column.type().contains("VARCHAR") || column.type().contains("TEXT") || column.type().contains("TIMESTAMP")) && objField.get(params) != null) {
                getObj.append(ConfigSql.APOSTROPHE);
                getObj.append((objField.get(params)));
                getObj.append(ConfigSql.APOSTROPHE);
            } else {
                getObj.append((objField.get(params)));
            }
            System.out.println(getObj);
            PreparedStatement stt = connection.prepareStatement(getObj.toString());
            ResultSet rst = stt.executeQuery();
            while (rst.next()) {
                T t = type.newInstance();
                loadResultSetIntoObject(rst, t);// Point 4
                obj = t;
            }
        } catch (SQLException | InstantiationException | IllegalAccessException throwables) {
            throwables.printStackTrace();
        }
        return obj;
    }
    public boolean isHasObj(String columnName,T obj){
        return findByColumns(columnName, obj) != null;
    }
    public boolean update(int id, T obj) {
        try {
            StringBuilder getObj = new StringBuilder();
            Class<?> objC = obj.getClass();

            getObj.append(ConfigSql.UPDATE);
            getObj.append(ConfigSql.SPACE);
            if (!type.isAnnotationPresent(Table.class)) {
                return false;
            }
            Table table = (Table) type.getDeclaredAnnotation(Table.class);
            if (table.name().length() > 0) {
                getObj.append(table.name());
            } else {
                getObj.append(type.getSimpleName().toLowerCase()).append("s");
            }
            getObj.append(ConfigSql.SPACE);
            getObj.append(ConfigSql.SET);
            getObj.append(ConfigSql.SPACE);
            //loop
            Field[] fields = objC.getDeclaredFields();


            for (Field field : fields
            ) {
                if (!field.isAnnotationPresent(Column.class)) {
                    continue;
                }
                if (field.getName().contains("id")) {
                    continue;
                }
                StringBuilder setOne = new StringBuilder();
                Column column = field.getDeclaredAnnotation(Column.class);

                if (column.name().length() > 0) {
                    setOne.append(column.name());
                } else {
                    setOne.append(field.getName());
                }
                setOne.append(ConfigSql.SPACE);
                setOne.append(ConfigSql.EQUAL_SIGN);
                setOne.append(ConfigSql.SPACE);


                field.setAccessible(true);
                if ((column.type().contains("VARCHAR") || column.type().contains("TEXT") || column.type().contains("TIMESTAMP")) && field.get(obj) != null) {
                    setOne.append(ConfigSql.APOSTROPHE);
                    setOne.append((field.get(obj)));
                    setOne.append(ConfigSql.APOSTROPHE);
                } else {
                    setOne.append((field.get(obj)));
                }
                setOne.append(ConfigSql.COMMA);
                setOne.append(ConfigSql.SPACE);
                getObj.append(setOne);
            }
            getObj.setLength(getObj.length() - 2);
            getObj.append(ConfigSql.SPACE);
            getObj.append(ConfigSql.WHERE);
            getObj.append(ConfigSql.SPACE);
            getObj.append(ConfigSql.ID);
            getObj.append(ConfigSql.SPACE);
            getObj.append(ConfigSql.EQUAL_SIGN);
            getObj.append(ConfigSql.SPACE);
            getObj.append(id);
            System.out.println(getObj.toString());

            PreparedStatement stt = connection.prepareStatement(getObj.toString());
            stt.execute();
            return true;
        } catch (IllegalAccessException | SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    //xóa mềm
    public boolean delete(int id,int statusDeleteNumber) {
        try {
            StringBuilder getObj = new StringBuilder();
            getObj.append(ConfigSql.UPDATE);
            getObj.append(ConfigSql.SPACE);
            if (!type.isAnnotationPresent(Table.class)) {
                return false;
            }
            Table table = (Table) type.getDeclaredAnnotation(Table.class);
            if (table.name().length() > 0) {
                getObj.append(table.name());
            } else {
                getObj.append(type.getSimpleName().toLowerCase()).append("s");
            }
            getObj.append(ConfigSql.SPACE);
            getObj.append(ConfigSql.SET);
            getObj.append(ConfigSql.SPACE);
            getObj.append(ConfigSql.STATUS);
            getObj.append(ConfigSql.EQUAL_SIGN);
            getObj.append(ConfigSql.SPACE);
            getObj.append(statusDeleteNumber);
            getObj.append(ConfigSql.SPACE);
            getObj.append(ConfigSql.WHERE);
            getObj.append(ConfigSql.SPACE);
            getObj.append(ConfigSql.ID);
            getObj.append(ConfigSql.EQUAL_SIGN);
            getObj.append(id);
            System.out.println(getObj.toString());

            PreparedStatement stt = connection.prepareStatement(getObj.toString());
            stt.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void loadResultSetIntoObject(ResultSet rst, Object obj) throws SQLException, IllegalAccessException {
//        Class<?> clazz = obj.getClass();
        for (Field field : type.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getDeclaredAnnotation(Column.class);
                Object value = rst.getObject(column.name());
                Class<?> type = field.getType();
                if (type.isPrimitive()) {
                    Class<?> boxed = boxPrimitveClass(type);
                    value = boxed.cast(value);
                }
                field.set(obj, value);
            }

        }
    }

    private Class<?> boxPrimitveClass(Class<?> type) {
        if (type == int.class) {
            return Integer.class;
        } else if (type == long.class) {
            return Long.class;
        } else if (type == double.class) {
            return Double.class;
        } else if (type == float.class) {
            return Float.class;
        } else if (type == boolean.class) {
            return Boolean.class;
        } else if (type == byte.class) {
            return Byte.class;
        } else if (type == char.class) {
            return Character.class;
        } else if (type == short.class) {
            return Short.class;
        } else {
            String string = "class '" + type.getName() + "' is not a primitive";
            throw new IllegalArgumentException(string);
        }
    }
}
