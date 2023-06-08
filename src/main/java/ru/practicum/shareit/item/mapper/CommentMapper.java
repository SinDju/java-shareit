package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.CommentDtoRequest;
import ru.practicum.shareit.item.dto.CommentDtoResponse;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CommentMapper {
    public CommentDtoRequest toCommentDtoRequvest(Comment comment) {
        return new CommentDtoRequest(comment.getId(), comment.getText(),
                comment.getAuthor().getName(), comment.getCreated());
    }

    public Comment toComment(CommentDtoRequest commentDtoRequest, Item item, User author) {
        return new Comment(commentDtoRequest.getId(), commentDtoRequest.getText(), item,
                author, commentDtoRequest.getCreated());
    }

    public List<CommentDtoRequest> commentDtoList(List<Comment> commentList) {
        return commentList.stream()
                .map(CommentMapper::toCommentDtoRequvest)
                .collect(Collectors.toList());
    }

    public CommentDtoResponse toCommentDtoResponse(Comment comment) {
        return new CommentDtoResponse(comment.getId(), comment.getText(),
                comment.getAuthor().getName(), comment.getCreated());
    }
}
