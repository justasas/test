require 'simplecov'
SimpleCov.start

require 'spec_helper'

RSpec::Matchers.define :be_in_chronological_order do
  match do |elements|
    chronological?(elements)
  end

  def chronological?(elements)
    element = elements.first
    elements.each do |next_element|
      return false if next_element.created_at < element.created_at
      element = next_element
    end
    true
  end
end

describe Topic do
  let(:user) { FactoryGirl.create(:account) }
  let(:user2) { FactoryGirl.create(:account) }
  let(:reply1) { FactoryGirl.create(:reply) }
  let(:reply2) { FactoryGirl.create(:reply) }
  let(:topic) { FactoryGirl.create(:topic_with_reply) }

  after(:each) { Account.destroy_all }
  after(:each) { Topic.destroy_all }
  after(:each) { Reply.destroy_all }

  it 'should have title' do
    topic2 = FactoryGirl.build(:topic)
    expect(topic2.title).to eq 'title111111'
  end

  it 'should have owner' do
    expect(topic.account).to eq Account.first
  end

  it "should have creator's reply when created" do
    expect(topic.account.replies.last).to eq topic.replies.first
  end

  describe '#add_reply' do
    it 'should add reply to the end of replies array' do
      reply3 = Reply.new('account' => user2, 'text' => 'text3')
      topic.add_reply(reply3)
      expect(topic.replies.last).to eq reply3
    end

    it 'should add replies in chronological order' do
      topic
      sleep 1
      reply3 = Reply.new('account' => user2, 'text' => 'text3')
      topic.add_reply(reply3)
      expect(topic.replies).to be_in_chronological_order
    end
  end

  describe '#add_rating' do
    it "should add user's rating if it doesn't exist" do
      topic.add_rating(user, 5)
      expect(user.ratings[0].rating).to eq 5
    end

    it 'should not add new rating if it already exists' do
      topic.add_rating(user, 3)
      topic.add_rating(user, 3)
      expect(topic.ratings.size).to eq 1
    end

    it "should change user's rating if it already exists" do
      topic.add_rating(user, 5)
      topic.add_rating(user, 4)
      expect(topic.ratings[0].rating).to eq 4
    end

    it 'should return not add rating if given rating is less than
    1 or bigger than 5' do
      topic.add_rating(user, 0)
      topic.add_rating(user, 0)
      expect(topic.ratings.size).to eq 0
    end
  end

  context 'when user with no privileges logged in' do
    describe '#remove_reply' do
      # it 'should return nil' do
      it 'should not remove reply' do
        reply = topic.replies[0]
        topic.remove_reply(user, reply)
        expect(topic.replies[0]).to eq reply
      end
    end

    describe '#edit_reply' do
      context 'when user wants to edit his own message' do
        it 'should change reply text field' do
          topic.edit_reply(Account.first, topic.replies[0], 'edited')
          expect(topic.replies[0].text).to eq 'edited'
        end
        it 'expects last_edit_time field to be > then time' do
          topic.edit_reply(Account.first, topic.replies[0], 'edited')
          expect(topic.replies[0].created_at < topic.replies[0].updated_at)
        end
      end

      context 'when user wants to edit other user message' do
        it 'should not change reply text field' do
          # expect(topic.edit_reply(user, 1, 'edited text field'))
          #  .to eq false        <-- pataisyt
          topic.add_reply(reply1)
          topic.edit_reply(Account.first, topic.replies[1], 'edited')
          expect(topic.replies[1].text).not_to eq 'edited'
        end
      end
    end
  end

  context 'when admin or moderator logged in' do
    describe '#remove_reply' do
      it 'should remove object from replies array' do
        topic.add_reply(reply1)
        user.update_attribute(:privilege, 'Administrator')
        topic.remove_reply(user, topic.replies[1])
        expect(Reply.all).not_to include reply1
      end
    end

    describe '#edit_reply' do
      it 'should change reply text field' do
        user.update_attribute(:privilege, 'Administrator')
        topic.edit_reply(user, topic.replies[0], 'edited text field')
        expect(topic.replies[0].text).to eq 'edited text field'
      end

      it 'V2 should change reply text field' do
        @reply = mock_model(Reply)
        @user = mock_model(Account, privilege: 'Administrator')

        expect(@reply).to receive(:update_attribute).twice
        topic.edit_reply(@user, @reply, 'edited text field')
      end

      it 'expects last_edit_time field to be > then time' do
        user.update_attribute(:privilege, 'Administrator')
        topic.edit_reply(user, topic.replies[0], 'edited text field')
        expect(topic.replies[0].created_at).to be < topic.replies[0].updated_at
      end
    end
  end
end
