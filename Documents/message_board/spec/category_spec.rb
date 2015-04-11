require 'simplecov'
SimpleCov.start

require 'spec_helper'

describe Category do
  let(:category) { FactoryGirl.create(:category) }
  let(:forum) { FactoryGirl.create(:forum) }
  let(:user) { FactoryGirl.create(:account) }

  describe '#add_forum' do
    it 'should add forum to the end of forums list when
      user is administrator' do
      user.privilege = 'Administrator'
      category.add_forum(forum, user)
      expect(category.forums.last).to eq forum
    end

    it 'should not add forum to the end of forums list when user
      is not administrator' do
      category.add_forum(forum, user)
      expect(category.forums).to be_empty
    end
  end

  describe '#remove_forum' do
    it 'should remove forum from the forums list when user is administrator' do
      forum2 = FactoryGirl.create(:forum)
      forum3 = FactoryGirl.create(:forum)
      user.privilege = 'Administrator'
      category.add_forum(forum, user)
      category.add_forum(forum2, user)
      category.add_forum(forum3, user)
      category.remove_forum(forum2.id, user)
      expect(category.forums).to contain_exactly(forum, forum3)
    end

    it 'should not remove forum from the forums list when user
      is not administrator' do
      forum2 = FactoryGirl.create(:forum)
      forum3 = FactoryGirl.create(:forum)
      user.privilege = 'Administrator'
      category.add_forum(forum, user)
      category.add_forum(forum2, user)
      category.add_forum(forum3, user)
      user.privilege = ''
      category.remove_forum(forum.id, user)
      expect(category.forums).to contain_exactly(forum, forum2, forum3)
    end
  end
end
