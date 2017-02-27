package com.wz.spider.db;

import com.wz.spider.entity.Article;

import java.sql.*;
import java.util.List;

/**
 * Created by wz on 2017/1/19.
 */
public class OsChinaDb {
    private Connection mConnection;

    private static final String CREATE_TABLE = "create table if not exists article(id integer primary key autoincrement, title text, author text, link text, summary text, date text)";

    public OsChinaDb() {
        try {
            Class.forName("org.sqlite.JDBC");
            mConnection = DriverManager.getConnection("jdbc:sqlite:D:/test.db");
            Statement statement = mConnection.createStatement();
            statement.executeUpdate(CREATE_TABLE);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void insert(Article article) {
        try {
            if (mConnection != null && !mConnection.isClosed()) {
                Statement statement = mConnection.createStatement();
                statement.executeUpdate("insert into article (title, author, link, summary, date) values ("
                        + article.title + ", " + article.author + ", " + article.link + ", " + article.summary + ", " + article.date + ")");
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert(List<Article> articles) {
        try {
            if (mConnection != null && !mConnection.isClosed()) {
                Statement statement = mConnection.createStatement();
                for (Article article : articles) {
                    ResultSet resultSet = statement.executeQuery("select * from article where link = '" + article.link + "'");
                    if (!resultSet.next()) {
                        PreparedStatement preparedStatement = mConnection.prepareStatement("insert into article(title, author, link, summary, date) values(?,?,?,?,?)");
                        preparedStatement.setString(1, article.title);
                        preparedStatement.setString(2, article.author);
                        preparedStatement.setString(3, article.link);
                        preparedStatement.setString(4, article.summary);
                        preparedStatement.setString(5, article.date);
                        preparedStatement.executeUpdate();
                        preparedStatement.close();
                    }
                    resultSet.close();
                }
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (mConnection != null && !mConnection.isClosed()) {
            mConnection.close();
        }
    }

}
