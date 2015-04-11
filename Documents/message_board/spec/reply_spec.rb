require 'simplecov'
SimpleCov.start

require 'spec_helper'

RSpec.describe Reply do
  let(:account) { FactoryGirl.build(:account) }
  let(:reply) { FactoryGirl.create(:reply) }

  before(:all) { Account.destroy_all }
  before(:all) { Reply.destroy_all }
  after(:each) { Account.destroy_all }
  after(:each) { Reply.destroy_all }

  it 'should have owner' do
    expect(reply.account).to eq Account.first
  end

  it 'should have text' do
    expect(reply.text == 'asddsadasd')
  end

  it 'should not have quote when not given' do
    expect(reply.replies).to be_empty
  end

  it 'should have quotes when given' do
    reply2 = FactoryGirl.create(:reply)
    reply.replies = [reply2]
    expect(reply.replies).not_to be_empty
  end

  describe '#to_string' do
    it 'should return correct string' do
      expect(reply.to_string)
       .to eq " reply owner: #{reply.account.name}\n reply text: #{reply.text}"
    end
  end
end
