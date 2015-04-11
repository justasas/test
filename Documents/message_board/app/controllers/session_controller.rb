# sesion_controller.rb
class SessionController < ApplicationController
  def new
    @account = Account.new
  end

  def create
    @internet_forum = InternetForum.first
    @account = @internet_forum.log_in(params[:account][:name],
                                      params[:account][:pass])
    if @account == 'user does not exist' || @account == 'pasword is not correct'
      redirect_to url_for(controller: :session, action: :new)
    else
      @forum = Forum.last
      session[:user_id] = @account.id
      redirect_to topics_path(@forum)
    end
  end

  def destroy
    if !session[:user_id].nil?
      session[:user_id] = nil
    else
      flash[:warning] = 'no one was logged in'
    end
    @forum = Forum.first
    redirect_to topics_path(@forum)
  end
end
