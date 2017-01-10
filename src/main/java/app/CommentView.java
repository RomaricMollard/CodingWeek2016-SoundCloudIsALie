package app;


import java.util.HashMap;
import java.util.LinkedList;

import com.google.api.services.youtube.model.Comment;
import com.google.api.services.youtube.model.CommentSnippet;

import core.MyChannel;
import javafx.scene.web.WebEngine;


public class CommentView extends View {

	LinkedList<Comment> commentInfo;
	int commentId;
	HashMap<Integer, Comment> comments;


	public CommentView(WebEngine jfxWebEngine, LinkedList<Comment> commentInfo, HashMap<Integer, Comment> comments, int commentId) {
		this.jfxWebEngine = jfxWebEngine;
		this.commentInfo = commentInfo;
		this.comments = comments;
		this.commentId = commentId;
		createBridge(new CommentViewBridge(), "CommentViewBridge");
	}

	@Override
	public void justAdded() {

		empty("commentid" + getId());

		boolean isFirst = true;
		for (Comment subCommentInfo : commentInfo) {

			if (!isFirst) {

				synchronized (comments) {
					int newCommentId = comments.size();
					comments.put(newCommentId, subCommentInfo);
					append(new SubCommentView(jfxWebEngine, subCommentInfo, newCommentId), "commentid" + commentId);
				}

			}
			isFirst = false;

		}
	}

	@Override
	public String getHTML() {

		CommentSnippet snippet = commentInfo.get(0).getSnippet();

		HashMap<String, String> info = new HashMap<String, String>();
		info.put("author", snippet.getAuthorDisplayName());
		info.put("comment", snippet.getTextDisplay());
		info.put("user_image", snippet.getAuthorProfileImageUrl());
		info.put("comment_id", "" + commentId);
		info.put("can_modify", ("http://www.youtube.com/channel/" + MyChannel.getId()).equals(snippet.getAuthorChannelUrl()) ? "inline-block" : "none");
		info.put("channel_thumbnailurl", App.myChannel.getMyChannel().getThumbnail());

		return doTemplate(View.getHTMLFileContent("comment.html"), info);

	}


	public class CommentViewBridge extends Bridge {
	}
}

