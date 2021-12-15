package rs.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import rs.model.Comment;

import java.net.URL;
import java.util.ResourceBundle;

import static rs.Main.userDAO;

public class CommentController implements Initializable {

    private Comment commentToEdit;

    @FXML
    private TextArea editCommentField;

    @FXML
    void saveCommentChanges(ActionEvent event) {
        String newText = editCommentField.getText();
        userDAO.updateComment(newText,commentToEdit.getCommentId());
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
        stage.getOnCloseRequest().handle(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    public void setCommentToEdit(Comment comment) {
        this.commentToEdit = comment;
        editCommentField.setText(comment.getCommentText());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}

