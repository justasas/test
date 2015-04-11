# topics_controller.rb
class TopicsController < ApplicationController
  def new
    if !session[:user_id].nil?
      @topic = Topic.new
    else
      # @forum = Forum.first
      redirect_to new_session_path
    end
  end

  def create
    if !session[:user_id]
      redirect_to new_session_path
    else
      @forum = Forum.first
      @account = Account.find(session[:user_id])
      @reply = Reply.create text: params[:description], account: @account
      @topic = @forum.topics.create(topic_params)
      @topic.add_reply(@reply)
      @topic.save
      redirect_to topics_path(@topic)
    end
  end

  def show
    @topic = Topic.find(params[:id])
  end

  def index
    @topics = Topic.all
  end

  def edit
    unless session[:user_id]
      return redirect_to new_session_path
    end
    @user = Account.find(session[:user_id])
    @topic = Topic.find(params[:id])
    if @user.privilege != 'Administrator' && @topic.account != @user
      flash[:error] = 'You are not the owner of this reply!'
    end
    @topic
  end

  def update
    @topic = Topic.find(params[:id])

    if @topic.update(topic_params)
      redirect_to @topic
    else
      render 'edit'
    end
  end

  private

  def topic_params
    params.require(:topic).permit(:title)
  end
end
