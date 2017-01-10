package core;


import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.api.services.youtube.model.Comment;
import com.google.api.services.youtube.model.CommentListResponse;
import com.google.api.services.youtube.model.CommentSnippet;
import com.google.api.services.youtube.model.CommentThread;
import com.google.api.services.youtube.model.CommentThreadListResponse;
import com.google.api.services.youtube.model.CommentThreadReplies;
import com.google.api.services.youtube.model.CommentThreadSnippet;

/* list of comment and modification*/


public class CommentList {


	private List<CommentThread> videoComments;
	private String videoId;
	private LinkedList<LinkedList<Comment>> comments;



	public CommentList(String videoId) {
		this.videoId = videoId;
		update();
		updateComments();
	}


	public void update() {
		try {
			CommentThreadListResponse videoCommentsListResponse;
			videoCommentsListResponse = YT.api.commentThreads()
					.list("snippet").setVideoId(videoId).setTextFormat("plainText").execute();
			videoComments = videoCommentsListResponse.getItems();
		}
		catch (IOException e) {
			System.out.println("Youtube API error detected : "+e.getMessage());
			//e.printStackTrace();
		}
	}


	public LinkedList<LinkedList<Comment>> getComments() {
		return comments;
	}

	public void updateComments() {
		
		if(videoComments == null){
			return;
		}
		
		comments = new LinkedList<LinkedList<Comment>>();
		try {

			CommentListResponse commentsListResponse = null;
			for (CommentThread videoComment : videoComments) {
				List<Comment> listcomment = new ArrayList<Comment>();
				commentsListResponse = YT.api.comments().list("snippet")
						.setParentId(videoComment.getSnippet().getTopLevelComment().getId()).setTextFormat("plainText").execute();
				listcomment = commentsListResponse.getItems();
				LinkedList<Comment> innerArrayList = new LinkedList<Comment>();
				innerArrayList.add(videoComment.getSnippet().getTopLevelComment());
				for (Comment comment : listcomment) {
					innerArrayList.add(comment);
				}

				comments.add(innerArrayList);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void printf() {
		if (videoComments.isEmpty()) {
			System.out.println("Can't get video comments.");
		}
		else {

			for (CommentThread videoComment : videoComments) {
				CommentSnippet snippet = videoComment.getSnippet().getTopLevelComment()
						.getSnippet();
				System.out.println("  - Author: " + snippet.getAuthorDisplayName());
				System.out.println("  - Comment: " + snippet.getTextDisplay());
			}
		}
	}

	/* return the list of comment*/
	public List<CommentThread> getCommentList() {
		return videoComments;
	}

	public Comment replyComment(Comment comment, String text) {
		String parentId = comment.getId();
		CommentSnippet commentSnippet = new CommentSnippet();
		commentSnippet.setTextOriginal(text);
		commentSnippet.setParentId(parentId);
		Comment mycomment = new Comment();
		mycomment.setSnippet(commentSnippet);
		try {
			Comment rcomment = YT.api.comments().insert("snippet", mycomment).execute();
			return rcomment;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
		//update();
	}


	public void deleteComment(Comment comment) {
		try {
			YT.api.comments().delete(comment.getId()).execute();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		//update();
	}




	public void changeComment(Comment changingComments, String text) {
		changingComments.getSnippet().setTextDisplay(text);
		changingComments.getSnippet().setTextOriginal(text);
		try {
			YT.api.comments().update("snippet", changingComments).execute();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}

	public Comment sendComment(String text) {
		CommentSnippet commentSnippet = new CommentSnippet();
		commentSnippet.setTextOriginal(text);
		Comment topLevelComment = new Comment();
		topLevelComment.setSnippet(commentSnippet);
		CommentThreadSnippet commentThreadSnippet = new CommentThreadSnippet();
		commentThreadSnippet.setTopLevelComment(topLevelComment);
		CommentThread commentThread = new CommentThread();
		commentThread.setSnippet(commentThreadSnippet);
		commentThreadSnippet.setVideoId(videoId);
		try {
			CommentThread videoCommentInsertResponse = YT.api.commentThreads()
					.insert("snippet", commentThread).execute();
			return videoCommentInsertResponse.getSnippet().getTopLevelComment();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}




	public List<CommentSnippet> getReplySnipet(CommentThread listComment) {
		CommentListResponse commentsListResponse = null;
		try {
			commentsListResponse = YT.api.comments().list("snippet")
					.setParentId(listComment.getSnippet().getTopLevelComment().getId()).setTextFormat("plainText").execute();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		List<Comment> comments = commentsListResponse.getItems();

		List<CommentSnippet> listSnippet = new ArrayList<CommentSnippet>();
		for (Comment commentReply : comments)
			listSnippet.add(commentReply.getSnippet());
		return listSnippet;
	}

	public CommentThreadReplies getReplyComment(CommentThread ListComment) {
		return ListComment.getReplies();
	}


	/*used in front*/




	/*
	public void replyComment(int indice, String text) {
		CommentThread thread2Answer = videoComments.get(indice);
		String parentId = thread2Answer.getId();
		CommentSnippet commentSnippet = new CommentSnippet();
		commentSnippet.setTextOriginal(text);
		commentSnippet.setParentId(parentId);
		Comment mycomment = new Comment();
		mycomment.setSnippet(commentSnippet);
		try {
			Comment commentInsertResponse = YT.api.comments().insert("snippet", mycomment)
					.execute();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		//update();
	}*/


	/*
	public void deleteComment(int indice) {
		CommentThread changingComment = videoComments.get(indice);
		Comment Topcomment = changingComment.getSnippet().getTopLevelComment();
		try {
			YT.api.comments().delete(Topcomment.getId()).execute();
	
			System.out.println(Topcomment.getId());
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		//update();
	}
	*/
	/*
		public void changeComment(CommentThread thread, CommentSnippet snippet, String text) {
			CommentListResponse commentsListResponse;
			try {
				commentsListResponse = YT.api.comments().list("snippet")
						.setParentId(thread.getSnippet().getTopLevelComment().getSnippet().getVideoId()).setTextFormat("plainText").execute();
				List<Comment> comments = commentsListResponse.getItems();
				Comment comment = new Comment();
				for (Comment c : comments) {
					if (c.getSnippet().equals(snippet)) {
						comment = c;
					}
					comment.getSnippet().setTextOriginal(text);
				}
				YT.api.comments().update("snippet", comment).execute();
	
			}
			catch (IOException e) {
	
				e.printStackTrace();
			}
		}*/
	/*
		public void changeComment(CommentThread thread, String text) {
			thread.getSnippet().getTopLevelComment().getSnippet().setTextOriginal(text);
			try {
				YT.api.comments().update("snippet", thread.getSnippet().getTopLevelComment()).execute();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			//update();
		}
		*/
	/*
		public void changeCommentind(int indice, String text) {
			CommentThread changingComment = videoComments.get(indice);
			changingComment.getSnippet().getTopLevelComment().getSnippet()
					.setTextOriginal(text);
			try {
				CommentThread videoCommentUpdateResponse = YT.api.commentThreads()
						.update("snippet", changingComment).execute();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			//update();
		}
	*/



}
