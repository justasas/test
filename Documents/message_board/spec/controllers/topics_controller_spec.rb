require 'simplecov'
SimpleCov.start

require 'rails_helper'

RSpec.describe TopicsController, type: :controller do
  before :each do
    @forum = FactoryGirl.create(:forum)
  end

  describe 'GET index' do
    it 'renders the index template' do
      get :index
      expect(response).to render_template('index')
    end

    it 'has a 200 status code' do
      get :index
      expect(response.status).to eq(200)
    end
  end

  describe 'GET new' do
    it 'return new topic when session[:user_id] is not nil' do
      session[:user_id] = 45
      expect(Topic).to receive(:new)
      get :new
    end

    it 'redirects to login if session[:user_id] is nil' do
      get :new
      expect(response).to redirect_to new_session_path
    end
  end

  describe 'GET edit' do
    it 'sets flash notice' do
      topic = FactoryGirl.create(:topic_with_reply)
      @account = FactoryGirl.create(:account, name: 'name', email: 'e@e.com')
      @account2 = FactoryGirl.create(:account, name: 'name2', email: 'e2@e.com')
      topic.account = @account
      session[:user_id] = @account2.id
      get :edit, id: topic.id
      expect(flash[:error]).to be_present
    end

    it 'redirects to login if session[:user_id] is nil' do
      topic = FactoryGirl.create(:topic_with_reply)
      get :edit, id: topic.id
      expect(response).to redirect_to new_session_path
    end
  end

  describe 'GET show' do
    it 'should assign topic' do
      topic = FactoryGirl.create(:topic_with_reply)
      get :show, id: topic.id
      expect(assigns(:topic)).not_to be_nil
    end
  end

  describe 'POST create' do
    it 'should create a new topic if user is not nil' do
      @account = FactoryGirl.create(:account, name: 'name', email: 'e@e.com')
      session[:user_id] = @account.id
      expect do
        post 'create', topic: { title: '0123456789' }
      end.to change(Topic, :count).by(1)
    end

    it 'Ver2 should create a new topic if user is not nil' do
      @account = FactoryGirl.create(:account, name: 'name', email: 'e@e.com')
      session[:user_id] = @account.id

      # topic = stub_model(Topic)
      # topic.stub(:add_reply)

      # forum = mock_model(Forum)
      # Forum.stub(:first).and_return(forum)
      # forum.stub(:topics)
      # forum.topics.stub(:create).and_return(topic)

      #expect(topic).to receive(:save)

      expect_any_instance_of(Topic).to receive(:save).twice
      post 'create', topic: { title: '0123456789' }
    end

    it 'should not create a new topic user is nil' do
      expect do
        post 'create', topic: { title: '0123456789' }
      end.not_to change(Topic, :count)
    end

    it 'should redirect to new session if user is nil' do
      post 'create', topic: { title: '0123456789' }
      expect(response).to redirect_to new_session_path
    end

    it 'should not redirect to new session if user is not nil' do
      @account = FactoryGirl.create(:account, name: 'name', email: 'e@e.com')
      session[:user_id] = @account.id
      post 'create', topic: { title: '0123456789' }
      expect(response).not_to redirect_to new_session_path
    end
  end
end
