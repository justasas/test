# replies_controller.rb
class RepliesController < ApplicationController
  def create
    if !session[:user_id]
      redirect_to new_session_path
    else
      topic = Topic.find(params[:topic_id].to_i)
      account = Account.find(session[:user_id])
      reply = topic.replies.create(reply_params)
      reply.account = account
      reply.save
      redirect_to topic_path(topic)
    end
  end

  private

  def reply_params
    params.require(:reply).permit(:text)
  end
end
