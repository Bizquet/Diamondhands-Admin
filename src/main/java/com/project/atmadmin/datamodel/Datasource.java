package com.project.atmadmin.datamodel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Datasource {

    private enum ClientInfoConstants{
        TABLE("atm_db.client_infos"),
        CLIENT_ID("client_infos.client_id"),
        FNAME("client_infos.fname"),
        LNAME("client_infos.lname"),
        OCCUPATION("client_infos.occupation"),
        ADDRESS("client_infos.address"),
        EMAIL("client_infos.email");

        final String constant;

        ClientInfoConstants(String constant){
            this.constant = constant;
        }

        @Override
        public String toString() {
            return constant;
        }
    }

    private enum AccountsConstants{
        TABLE("atm_db.accounts"),
        UID("accounts.uid"),
        HOLDER("accounts.acc_holder");

        final String constant;

        AccountsConstants(String constant) {
            this.constant = constant;
        }

        @Override
        public String toString() {
            return constant;
        }
    }

    private static Datasource instance = new Datasource();

    private static final String CONNECTION_STRING = "jdbc:mysql://127.0.01:3306/atm_db";
    private static final String USERNAME = "root";
    private static final String  PASSWORD = "Violet_1337";

    private static final String DELETE_CLIENT_INFO = "DELETE FROM " + ClientInfoConstants.TABLE + " WHERE " +
            ClientInfoConstants.CLIENT_ID + " = ?";
    private static final String UPDATE_CLIENT_INFO = "UPDATE " + ClientInfoConstants.TABLE + " SET " +
            ClientInfoConstants.FNAME + " = ?, " + ClientInfoConstants.LNAME + " = ?, " +
            ClientInfoConstants.OCCUPATION + " = ?, " + ClientInfoConstants.ADDRESS + " = ?, " +
            ClientInfoConstants.EMAIL + " = ? " + " WHERE " + ClientInfoConstants.CLIENT_ID + " = ?";

    private Connection con;
    private PreparedStatement deleteClientInfo;
    private PreparedStatement updateClientInfo;

    private Datasource(){

    }

    // makes the class a singleton class
    public static Datasource getInstance(){
        return instance;
    }

    /**
     * initiates the connections to the db
     * @return true if a connection is established, else false
     */
    public boolean open() {
        try {
            con = DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD);
            deleteClientInfo = con.prepareStatement(DELETE_CLIENT_INFO);
            updateClientInfo = con.prepareStatement(UPDATE_CLIENT_INFO);
            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't connect to database" + e.getMessage());
            e.printStackTrace();

            return false;
        }
    }

    /**
     * closes all the connections to the database
     */
    public void close(){
        try{
            if(con != null){
                con.close();
                System.out.println("connection established and closed");
            }

            if(deleteClientInfo != null){
                deleteClientInfo.close();
            }

            if (updateClientInfo != null){
                updateClientInfo.close();
            }

        } catch (SQLException e){
            System.out.println("Couldn't close connection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * deletes the client from the clients table
     * @param client_uid uid of the client to be deleted
     * @throws SQLException
     */
    public void deleteClient(int client_uid) throws SQLException{
        String query = "DELETE FROM atm_db.clients WHERE uid = ?";

        try(PreparedStatement statement = con.prepareStatement(query)){
            statement.setInt(1, client_uid);

            int affectedRows = statement.executeUpdate();

            if (affectedRows != 1){
                throw new SQLException("Error Deleting Client");
            }else {
                System.out.println("Successfully deleted clientuid: " + client_uid);
            }
        }
    }

    /**
     * finds the accounts of the client and deletes them from the accounts table
     * @param client_uid uid of the client to be deleted
     * @throws SQLException
     */
    public void deleteAccount(int client_uid) throws SQLException {

        String gatherUidQuery = "SELECT " + AccountsConstants.UID + " FROM " + AccountsConstants.TABLE +
                " WHERE " + AccountsConstants.HOLDER + " = ?";
        List<Integer> accUidList = new ArrayList<>();

        try(PreparedStatement statement = con.prepareStatement(gatherUidQuery)){
            statement.setInt(1, client_uid);

            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()){
                    accUidList.add(resultSet.getInt(1));
                }
            }

        }catch (SQLException e){
            System.out.println("Error getting acc uid");
            e.printStackTrace();
        }

        for (int accUid: accUidList) {
            System.out.println("Current accUid: " + accUid);
            String deleteQuery = "DELETE FROM " + AccountsConstants.TABLE + " WHERE " +
                    AccountsConstants.UID + " = ?";

            try (PreparedStatement statement = con.prepareStatement(deleteQuery)) {
                statement.setInt(1, accUid);

                int affectedRows = statement.executeUpdate();

                if (affectedRows != 1) {
                    throw new SQLException("Error Deleting Client");
                } else {
                    System.out.println("Successfully deleted clientuid's account: " + accUid);
                }
            }
        }

    }

    /**
     * Uses a transaction to safely delete all data across all tables, finally deletes the client info
     * if deleting from clients and accounts table is successful
     * @param client_uid uid of the client to be deleted
     * @return true if transaction is successful, else false
     */
    public boolean deleteClientInfo(int client_uid){
        try{
            con.setAutoCommit(false);

            deleteClient(client_uid);

            deleteAccount(client_uid);

            deleteClientInfo.setInt(1, client_uid);

            int affectedRows = deleteClientInfo.executeUpdate();

            if(affectedRows != 1){
                throw new SQLException("Error Deleting Client Info");
            } else{
                System.out.println("Successfully deleted all Client Info");
                con.commit();
                return true;
            }

        } catch (Exception e){
            System.out.println("Insert client Exception: " + e.getMessage());
            e.printStackTrace();
            try{
                // aborts the transaction and rollbacks any change we made
                System.out.println("Performing rollback");
                con.rollback();
            } catch (SQLException e2){
                System.out.println("Ohhh mahh gahd" + e.getMessage());
                e.printStackTrace();
            }
        } finally {
            System.out.println("Setting auto commit to true");
            try{
                // turns on the autocommit again after transaction ends
                con.setAutoCommit(true);
            }catch (SQLException e3){
                System.out.println("Couldn't reset autocommit " + e3.getMessage());
            }
        }

        return false;
    }

    /**
     * Updates the client info table with the new values
     * @param client_uid uid of the client
     * @param info ClientInfo object that contains all the info
     */
    public void updateClientInfo(int client_uid, ClientInfo info){
        try {
            updateClientInfo.setString(1, info.getFirstName());
            updateClientInfo.setString(2, info.getLastName());
            updateClientInfo.setString(3, info.getOccupation());
            updateClientInfo.setString(4, info.getAddress());
            updateClientInfo.setString(5, info.getEmail());
            updateClientInfo.setInt(6, client_uid);

            int affectedRows = updateClientInfo.executeUpdate();

            if(affectedRows == 1){
                System.out.println("Successfully Updated Client info of " + client_uid);
            }else {
                System.out.println("Error updating client info of " + client_uid);
            }

        } catch (SQLException e){
            System.out.println("Error updating client info " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * checks if the inputted passoword is correct
     * @param password string inputted by the user
     * @return true if there is a match, else false
     */
    public boolean checkPassword(String password){
        String query = "SELECT admin.password FROM atm_db.admin WHERE admin.password = ?";

        boolean hasSimilar = false;

        try(PreparedStatement statement = con.prepareStatement(query)){
            statement.setString(1, password);

            try(ResultSet resultSet = statement.executeQuery()){
                hasSimilar = resultSet.next();
            }

        }catch (SQLException e){
            System.out.println("Error check for password " + e.getMessage());
            e.printStackTrace();
        }

        return hasSimilar;
    }

    /**
     * queries all data from the view
     * @return List of admindata to be displayed on the tableview
     */
    public List<AdminData> queryAdminData(){
        String query = "SELECT * FROM atm_db.admin_view";

        List<AdminData> adminList = new ArrayList<>();

        try(Statement queryAdminData = con.createStatement()){

            try(ResultSet resultSet = queryAdminData.executeQuery(query)){
                while (resultSet.next()){
                    AdminData adminData = new AdminData();
                    adminData.setClient_uid(resultSet.getInt(1));
                    adminData.setAcc_uid(resultSet.getInt(2));
                    adminData.setfName(resultSet.getString(3));
                    adminData.setlName(resultSet.getString(4));
                    adminData.setAddress(resultSet.getString(5));
                    adminData.setEmail(resultSet.getString(6));
                    adminData.setOccupation(resultSet.getString(7));
                    adminData.setAcc_type(resultSet.getString(8));

                    adminList.add(adminData);
                }
            }
        }catch (SQLException e){
            System.out.println("Error getting admin data " + e.getMessage());
            e.printStackTrace();
        }

        return adminList;
    }

}
