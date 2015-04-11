require 'simplecov'
SimpleCov.start

require 'rails_helper'

RSpec.describe SessionController, type: :controller do
  before :each do
    @account = FactoryGirl.create(:account, name: 'vardas', pass: 'slaptazodis',
                                                            email: 'e@e.com')
    @internet_forum = FactoryGirl.create(:internet_forum, accounts: [@account])
    @forum = FactoryGirl.create(:forum)
  end

  describe 'GET new' do
    it 'returns http success' do
      get :new
      expect(response).to have_http_status(:success)
    end
  end

  describe 'GET destroy' do
    it 'sets a flash notice if it destroys nothing' do
      session[:user_id] = nil
      get :destroy
      expect(flash[:warning]).to be_present
    end
  end

  describe 'POST #create' do
    context 'when user is not valid' do
      it 'redirects to the same page' do
        post 'create', account: { name: @account.name, pass: 'k' }
        expect(response).to redirect_to session_new_path
      end
    end

    context 'when user is valid' do
      it 'redirects to forum' do
        post :create, account: { name: @account.name, pass: @account.pass }
        expect(response).to redirect_to topics_path(@forum)
      end
    end
  end
end
