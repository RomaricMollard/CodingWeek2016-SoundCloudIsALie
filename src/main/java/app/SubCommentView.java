package app;


import java.util.HashMap;

import com.google.api.services.youtube.model.Comment;

import core.MyChannel;
import javafx.scene.web.WebEngine;


public class SubCommentView extends View {

	Comment commentInfo;
	int threadId;
	int subCommentId;


	public SubCommentView(WebEngine jfxWebEngine, Comment subCommentInfo, int subCommentId) {
		this.jfxWebEngine = jfxWebEngine;
		this.commentInfo = subCommentInfo;
		this.subCommentId = subCommentId;
		createBridge(new SubCommentViewBridge(), "SubCommentViewBridge");
	}

	@Override
	public String getHTML() {

		String authorChannelId = commentInfo.getSnippet().getAuthorChannelId().toString();

		HashMap<String, String> info = new HashMap<String, String>();
		info.put("author", commentInfo.getSnippet().getAuthorDisplayName());
		info.put("comment", commentInfo.getSnippet().getTextDisplay());
		info.put("user_image", commentInfo.getSnippet().getAuthorProfileImageUrl());
		info.put("comment_id", "" + subCommentId);
		info.put("can_modify", ("http://www.youtube.com/channel/" + MyChannel.getId()).equals(commentInfo.getSnippet().getAuthorChannelUrl()) ? "inline-block" : "none");

		return doTemplate(View.getHTMLFileContent("subcomment.html"), info);
	}


	public class SubCommentViewBridge extends Bridge {
	}
}
