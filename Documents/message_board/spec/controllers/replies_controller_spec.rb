require 'simplecov'
SimpleCov.start

require 'rails_helper'

RSpec.describe RepliesController, type: :controller do
  before :each do
    @account = FactoryGirl.create(:account, name: 'name', email: 'e@e.com')
    @reply = FactoryGirl.create(:reply, account: @account)
    @topic = FactoryGirl.create(:topic, replies: [@reply])
  end

  describe 'POST create' do
    it 'should create a new reply if session[:user_id] is not nil' do
      session[:user_id] = @account.id
      expect do
        post 'create', reply: { text: '0123456789' }, topic_id: @topic.id
      end.to change(Reply, :count)
    end

    it '2nd version. should create a new reply if session[:user_id]
    is not nil' do
      session[:user_id] = @account.id

      reply = double(Reply)
      reply.stub(:account=)
      reply.stub(:save)

      topic = mock_model(Topic)
      Topic.stub(:find).and_return(topic)
      topic.stub(:replies)
      topic.replies.stub(:create).and_return(reply)

      expect(reply).to receive(:save)
      post 'create', reply: { text: '0123456789' }, topic_id: 5000
    end

    it 'should not create a new reply if user is nil' do
      expect do
        post 'create', reply: { text: '0123456789' }, topic_id: @topic.id
      end.not_to change(Reply, :count)
    end

    it 'should redirect to session/new if user is nil' do
      post 'create', reply: { text: '0123456789' }, topic_id: @topic.id
      expect(response).to redirect_to new_session_path
    end

    it 'should not redirect to session/new if user is not nil' do
      session[:user_id] = @account.id
      post 'create', reply: { text: '0123456789' }, topic_id: @topic.id
      expect(response).not_to redirect_to new_session_path
    end
  end
end
