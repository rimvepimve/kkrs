package rs.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import rs.model.Comment;
import rs.model.Employee;
import rs.model.User;
import rs.util.DBUtil;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import static rs.Main.*;

public class MainController implements Initializable {

    @FXML
    private ListView<String> hairdressersList;

    @FXML
    private TreeView<String> commentsTree;

    @FXML
    private TextArea commentArea;

    @FXML
    void showProfile(ActionEvent event) {
        ArrayList<String> registrations =  new ArrayList<>();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/customerProfile.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage3 = new Stage();
            Scene scene = new Scene(root, 800, 800);
            stage3.setTitle("My Profile");
            stage3.setScene(scene);
            stage3.show();
        } catch (Exception e) {
            System.err.println(String.format("Error: %s", e.getMessage()));
        }

    }

    @FXML
    void goToRegistration(ActionEvent event) {
        Employee emp = (Employee)userDAO.getUserByFullName(hairdressersList.getSelectionModel().getSelectedItem());
        if(emp == null) {
            return;
        }
        int selectedEmployeeId = emp.getUserId();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/appointments.fxml"));
            Parent root = fxmlLoader.load();

            AppointmentsController controller = fxmlLoader.getController();
            controller.setEmployee_id(selectedEmployeeId);

            Stage stage2 = new Stage();

            stage2.setOnCloseRequest(
                    new EventHandler<WindowEvent>() {
                        public void handle(WindowEvent we) {
                            //loadComments();
                        }
                    });

            stage2.setUserData(selectedEmployeeId);
            Scene scene = new Scene(root, 600, 600);
            stage2.setTitle("Registration");
            stage2.setScene(scene);
            stage2.show();
        } catch (Exception e) {
            System.err.println(String.format("Error: %s", e.getMessage()));
        }
    }

    @FXML
    void deleteSelectedTreeItem(ActionEvent event) {
        Comment comment = getCommentFromTree();
        if (comment == null) return;
        if(comment.getAuthorId() == currentUser.getUserId()) {
            userDAO.deleteComment(comment.getCommentId());
            loadComments();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Access denied");
            alert.setHeaderText("You can not delete other user's comments.");
            alert.showAndWait();
        }
    }

    @FXML
    void editSelectedComment(MouseEvent event) {
        Comment comment = getCommentFromTree();
        if (comment == null) return;
        if (comment.getAuthorId() == currentUser.getUserId()) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/comment.fxml"));
                Parent root = fxmlLoader.load();

                CommentController controller = fxmlLoader.getController();
                controller.setCommentToEdit(comment);

                Stage stage1 = new Stage();

                stage1.setOnCloseRequest(
                        new EventHandler<WindowEvent>() {
                            public void handle(WindowEvent we) {
                                loadComments();
                            }
                        });

                stage1.setUserData(comment);
                Scene scene = new Scene(root, 600, 400);
                stage1.setTitle("Edit Comment");
                stage1.setScene(scene);
                stage1.show();
            } catch (Exception e) {
                System.err.println(String.format("Error: %s", e.getMessage()));
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Access denied");
            alert.setHeaderText("You can not edit other user's comments.");
            alert.showAndWait();
        }
    }

    private Comment getCommentFromTree(){
        if(commentsTree.getSelectionModel().getSelectedItem() == null)
            return null;
        String commentText = commentsTree.getSelectionModel().getSelectedItem().getValue();
        int commentId = userDAO.getCommentIdByText(commentText);
        return userDAO.getCommentByCommentId(commentId);
    }

    private void addCommentsToTree(TreeItem<String> root, Comment comment){
        TreeItem<String> newItem;
        for(Comment c: comment.getComments()){
            newItem = new TreeItem<>(c.getCommentText());
            root.getChildren().add(newItem);
            if(c.getComments().size() != 0) {
                addCommentsToTree(newItem, c);
            }
        }
    }

    public void loadComments() {
        Employee e = (Employee) userDAO.getUserByFullName(hairdressersList.getSelectionModel().getSelectedItem());
        TreeItem<String> root = new TreeItem<>("Atsiliepimai:");
        TreeItem<String> itemToAdd;
        if (e.getComments() != null)
            for(Comment c: e.getComments()){
                itemToAdd = new TreeItem<>(c.getCommentText());
                root.getChildren().add(itemToAdd);
                if(c.getComments().size() != 0) {
                    addCommentsToTree(itemToAdd, c);
                }
            }
        commentsTree.setRoot(root);
    }

    @FXML
    void loadUserComments(MouseEvent event) {
        loadComments();
    }

    @FXML
    void commentUser(ActionEvent event) {
    if (!commentArea.getText().trim().isEmpty()) {
      Comment newComment = new Comment();
      Employee e =(Employee)userDAO.getUserByFullName(hairdressersList.getSelectionModel().getSelectedItem());
      newComment.setAuthorId(currentUser.getUserId());
      newComment.setCommentText(commentArea.getText());
      newComment.setUserId(e.getUserId());
      commentArea.clear();
      e.getComments().add(newComment);
      commentsTree.refresh();
      userDAO.addNewCommentToDB(newComment);
    }
    loadComments();
  }

    private void findCommentToReplay(Comment newComment, Comment comment){
        String selectedItemText = commentsTree.getSelectionModel().getSelectedItem().getValue();
        for(Comment c: comment.getComments()){
            if(c.getCommentText().equals(selectedItemText)){
                c.getComments().add(newComment);
                userDAO.addReplyToComment(c.getCommentId(),newComment);
            } else if(c.getComments().size() != 0){
                findCommentToReplay(newComment, c);
            }
        }
    }

    @FXML
    void replyToComment(ActionEvent event) {
        if (!commentArea.getText().trim().isEmpty()){
            Comment newComment = new Comment();
            newComment.setAuthorId(currentUser.getUserId());
            newComment.setCommentText(commentArea.getText());
            Employee e = (Employee)userDAO.getUserByFullName(hairdressersList.getSelectionModel().getSelectedItem());
            for(Comment c: e.getComments()){
                if(c.getCommentText().equals(commentsTree.getSelectionModel().getSelectedItem().getValue())){
                    c.getComments().add(newComment);
                    userDAO.addReplyToComment(c.getCommentId(),newComment);
                } else if (c.getComments().size() != 0) {
                    findCommentToReplay(newComment, c);
                }
            }
            commentArea.clear();
            loadComments();
        }
    }

    @FXML
    void signOut(ActionEvent event) {
        try {
        Parent root = FXMLLoader.load(getClass().getResource("/LogIn.fxml"));
        Stage primaryStage = primary;
        Scene primaryScene = new Scene(root, 800, 800);
        primaryScene.getStylesheets().add("css/login.css");
        primaryStage.setTitle("Sign In");
        primaryStage.setScene(primaryScene);
        primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void deleteAccount(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Do you want to delete your profile?");
        alert.setContentText("Your profile will be permanently deleted.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            userDAO.delete(currentUser.getUserId());
            signOut(event);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        testDBConnection();
        for(User user: userDAO.getAllEmployees()) {
            hairdressersList.getItems().add(user.getFullName());
        }
    }

    private void testDBConnection() {
        try(Connection conn = new DBUtil().getConnection()) {
            System.out.println("Connected to database");
        }
        catch (SQLException e) {
            DBUtil.showErrorMessage(e);
        }
    }


}
