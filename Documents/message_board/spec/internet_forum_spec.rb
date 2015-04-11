require 'simplecov'
SimpleCov.start

require 'spec_helper'

describe InternetForum do
  let(:category) { FactoryGirl.create(:category) }
  let(:user) { FactoryGirl.create(:account) }
  let(:internet_forum) { FactoryGirl.create(:internet_forum) }

  after(:each) { Account.destroy_all }
  after(:each) { InternetForum.destroy_all }
  after(:each) { Category.destroy_all }

  it 'should have correct title' do
    expect(internet_forum.title).to eq 'MyString'
  end

  it 'should have correct description' do
    expect(internet_forum.description).to eq 'MyText0000000'
  end

  describe '#register_new_user' do
    it 'adds new user to forum when user with same name is
    not already registered' do
      internet_forum.register_new_user(user)
      expect(internet_forum.accounts.last).to eq user
    end

    it 'does not add new accout when user with same name is
    already registered' do
      user3 = FactoryGirl.create(:account)
      user.name = 'name'
      user3.name = 'name'
      internet_forum.register_new_user(user)
      internet_forum.register_new_user(user3)
      expect(internet_forum.accounts).not_to end_with user3
    end
  end

  describe '#log_in' do
    it 'should return correct account when log_in username and password
    are both correct' do
      internet_forum.register_new_user(user)
      expect(internet_forum.log_in(user.name, user.pass)).to eq user
    end

    it 'should return "user does not exist" when log_in username does
    not exist in forum' do
      internet_forum.register_new_user(user)
      expect(internet_forum.log_in('namade', 'pasdas1'))
        .to eq 'user does not exist'
    end

    it 'should return "password is not correct" when log_in password
    is not correct' do
      internet_forum.register_new_user(user)
      expect(internet_forum.log_in(user.name, 'incorrectpass'))
        .to eq 'pasword is not correct'
    end
  end

  describe '#add_category' do
    it 'should add category to the end of categories list when user
    is administrator' do
      user.privilege = 'Administrator'
      internet_forum.add_category(category, user)
      expect(internet_forum.categories.last).to eq category
    end

    it 'should not add category to the end of categories list when user
    is not administrator' do
      internet_forum.add_category(category, user)
      expect(internet_forum.categories).to be_empty
    end

    it 'should not add category if category with same name already exists
    when user is administrator' do
      user.privilege = 'Administrator'
      category.name = 'name'
      internet_forum.add_category(category, user)
      category2 = FactoryGirl.create(:category)
      category2.name = 'name'
      internet_forum.add_category(category2, user)
      expect(internet_forum.categories).to contain_exactly(category)
    end
  end

  describe '#remove_category' do
    it 'should remove category from the categories list when user
    is administrator' do
      category2 = FactoryGirl.create(:category)
      category3 = FactoryGirl.create(:category)
      user.privilege = 'Administrator'
      internet_forum.add_category(category, user)
      internet_forum.add_category(category2, user)
      internet_forum.add_category(category3, user)
      internet_forum.remove_category(category2.id, user)
      expect(internet_forum.categories).to contain_exactly(category, category3)
    end

    it 'should not remove category from the categories list when user
    is not administrator' do
      category2 = FactoryGirl.create(:category)
      category3 = FactoryGirl.create(:category)
      user.privilege = 'Administrator'
      internet_forum.add_category(category, user)
      internet_forum.add_category(category2, user)
      internet_forum.add_category(category3, user)
      user.privilege = ''
      internet_forum.remove_category(category.id, user)
      expect(internet_forum.categories)
        .to contain_exactly(category, category2, category3)
    end
  end

  describe '#remove_user' do
    it 'should remove account from the accounts list when current
    user is administrator' do
      user2 = FactoryGirl.create(:account)
      user3 = FactoryGirl.create(:account)
      internet_forum.register_new_user(user)
      internet_forum.register_new_user(user2)
      internet_forum.register_new_user(user3)
      user.privilege = 'Administrator'
      internet_forum.remove_user(user2.id, user)
      expect(internet_forum.accounts).to contain_exactly(user, user3)
    end

    it 'should not remove account from the accounts list when
      user is not administrator' do
      user2 = FactoryGirl.create(:account)
      user3 = FactoryGirl.create(:account)
      internet_forum.register_new_user(user)
      internet_forum.register_new_user(user2)
      internet_forum.register_new_user(user3)
      user3.privilege = ''
      internet_forum.remove_user(user.id, user3)
      expect(internet_forum.accounts).to contain_exactly(user, user2, user3)
    end

    it 'should remove account if user tries to delete his own account' do
      user2 = FactoryGirl.create(:account)
      user3 = FactoryGirl.create(:account)
      internet_forum.register_new_user(user)
      internet_forum.register_new_user(user2)
      internet_forum.register_new_user(user3)
      internet_forum.remove_user(user.id, user)
      expect(internet_forum.accounts).to contain_exactly(user2, user3)
    end
  end
end
