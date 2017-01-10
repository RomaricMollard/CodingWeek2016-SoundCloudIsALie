package app;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.api.services.youtube.model.Comment;
import com.google.api.services.youtube.model.Video;

import core.CommentList;
import core.VideoData;
import javafx.application.Platform;
import javafx.scene.web.WebEngine;
import utils.Utils;


public class CommentsView extends View {

	int nextThreadId;

	public CommentList commentsModel;
	public LinkedList<LinkedList<Comment>> commentsList;
	private HashMap<Integer, Comment> comments = new HashMap<Integer, Comment>();

	Thread initialization;


	public CommentsView(WebEngine jfxWebEngine) {
		this.jfxWebEngine = jfxWebEngine;
		nextThreadId = 0;
		createBridge(new CommentsViewBridge(), "CommentsViewBridge");
	}

	@Override
	public String getHTML() {
		return doTemplate(View.getHTMLFileContent("comments.html"), Utils.getMap(App.myChannel.getMyChannel()));
	}


	public class CommentsViewBridge extends Bridge {

		public void addComment(final String text) {

			if (text.equals("")) {
				return;
			}

			(new Thread() {
				public void run() {

					Comment comment = commentsModel.sendComment(text);

					commentsList.addFirst(new LinkedList<Comment>());
					commentsList.get(0).add(comment);

					updateComments();

				}

			}).start();

		}

		public void addSubComment(final String text, final String commentIdString) {

			if (text.equals("")) {
				return;
			}

			final int commentId = Integer.parseInt(commentIdString);

			(new Thread() {
				public void run() {

					Comment parentComment = comments.get(commentId);

					Comment comment = commentsModel.replyComment(parentComment, text);

					//Trouver et updater les commentaires
					for (LinkedList<Comment> comments : commentsList) {
						if (comments.get(0).getId().equals(parentComment.getId())) {
							comments.add(comment);
							break;
						}
					}

					updateComments();

				}
			}).start();
		}

		public void editComment(final String text, final String commentIdString) {

			if (text.equals("")) {
				return;
			}

			final int commentId = Integer.parseInt(commentIdString);

			(new Thread() {
				public void run() {

					Comment comment = comments.get(commentId);
					commentsModel.changeComment(comment, text);

					Platform.runLater(new Runnable() {

						public void run() {
							
							doJS("$('#comment_text" + commentId + "').html($('#editCommentTextArea" + commentId + "').val());");

						}
					});

				}
			}).start();
		}

		public void deleteComment(final String commentIdString) {

			final int commentId = Integer.parseInt(commentIdString);

			(new Thread() {
				public void run() {

					Comment comment = comments.get(commentId);
					commentsModel.deleteComment(comment);

					Platform.runLater(new Runnable() {

						public void run() {
							
							doJS("$('#comment" + commentId + "').parent().remove();");

						}
					});

				}
			}).start();
		}

	}



	/**
	 * Initialise le contenu de la partie commentaires
	 * 
	 * @param currentVideo
	 */
	public void init(final VideoData currentVideo) {
		

		comments.clear();

		Platform.runLater(new Runnable() {
			public void run() {
				empty("comments_list");
				doJS(".comments.loading", "show()");
				doJS("#new_comment", "hide()");

				doJS("commentViewChangeImage('" + currentVideo.getThumbnail().getMedium().getUrl() + "')");
				doTemplate(Utils.getMap(currentVideo));

			}
		});

		(new Thread() {
			public void run() {


				commentsModel = new CommentList(currentVideo.getId());
				commentsList = commentsModel.getComments();

				updateComments();


			}
		}).start();

	}

	private void updateComments() {
		
		if(App.player.currentVideo == null){
			return;
		}

		Platform.runLater(new Runnable() {

			public void run() {

				empty("comments_list");
				doJS(".comments.loading", "show()");
				doJS(".new_comment", "hide()");

				int commentId = 0;
				for (LinkedList<Comment> commentInfo : commentsList) {

					comments.put(commentId, commentInfo.get(0)); //Top comment
					append(new CommentView(jfxWebEngine, commentInfo, comments, commentId), "comments_list");

					++commentId;
				}
				
				String todo = "";
				
				todo += addCommentOnLine(App.player.currentVideo.getDescription());
				
				// Ajouter les commentaires sur la timebar
				for (LinkedList<Comment> comments : commentsList) {
					for (Comment comment : comments) {

						todo += addCommentOnLine(comment.getSnippet().getTextDisplay());
						
					}
				}
				
				doJS(todo);
				doJS("showCommentsLine();");

				doJS(".comments.loading", "hide()");
				doJS(".new_comment", "show()");
				doJS("commentsOpenSubResponse();");
				doJS("openEditComment();");

			}
		});

	}
	
	private String addCommentOnLine(String input){
		
		String todo = "";
		
		Pattern p = Pattern.compile("(\\d+):(\\d{2})(:(\\d{2}))?.*");
		Matcher m = p.matcher(input);

		while (m.find()) {
			int time = 0;
			String timeTxt;
			if(m.group(3)!=null){
				timeTxt = m.group(1)+":"+m.group(2)+":"+m.group(4);
				time += Integer.parseInt(m.group(1))*60*60+Integer.parseInt(m.group(2))*60+Integer.parseInt(m.group(4));
			}else{
				timeTxt = m.group(1)+":"+m.group(2);
				time += Integer.parseInt(m.group(1))*60+Integer.parseInt(m.group(2));
			}
			String text = timeTxt+" - "+m.group(0).substring(0, Math.min(m.group(0).length()-1,100)).replaceAll("(.*?)\\d+:\\d+(:\\d+)?", "$1");
		    todo += "addCommentOnLine('"+(time)+"','"+text.replace("'", "\\'").replace("\n", "").replace("\r", "")+"');";
		}
		
		return todo;
		
	}


}
