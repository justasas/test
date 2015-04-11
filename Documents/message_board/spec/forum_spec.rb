require 'simplecov'
SimpleCov.start

require 'spec_helper'

describe Forum do
  let(:user) { FactoryGirl.create(:account) }
  let(:topic) { FactoryGirl.create(:topic_with_reply) }
  let(:forum) { FactoryGirl.create(:forum) }

  after(:each) { Account.destroy_all }

  describe '#create_topic' do
    it 'should add topic to the end of topics list' do
      forum2 = FactoryGirl.build(:forum)
      topic2 = mock_model(Topic)
      forum2.topics = [topic2]
      expect(forum2.topics).to receive(:push).with(an_instance_of(Topic))
      forum2.create_topic(topic)
    end
  end

  describe '#delete_topic' do
    it 'should remove topic from the topics list when user is administrator
      or moderator' do
      forum2 = FactoryGirl.build(:forum)
      topic = mock_model(Topic)
      forum2.topics.push topic
      user = mock_model(Account, privilege: 'Administrator')
      expect(forum2.topics).to receive(:delete)
      forum2.delete_topic(0, user)
    end

    it 'should not remove topic from the topics list when user is administrator
      or moderator' do
      forum2 = FactoryGirl.build(:forum)
      topic = mock_model(Topic)
      forum2.topics.push topic
      expect(forum2.topics).not_to receive(:delete)
      forum2.delete_topic(0, user)
    end
  end
end
