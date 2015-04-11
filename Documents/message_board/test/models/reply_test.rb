require 'simplecov'
SimpleCov.start

require 'spec_helper'

describe Reply do
  let(:owner) { Account.new 'john', 'pass1', 'email@email.com', 1993 }
  let(:reply) { Reply.new(owner, 'text') }

  it 'should have correct Id' do
    expect(reply.Id == 1)
  end

  it 'should have owner' do
    expect(reply.owner == owner)
  end

  it 'should have text' do
    expect(reply.text == 'text')
  end

  it 'should not have quote when not given' do
    expect(reply.quotes).to be_empty
  end

  it 'should have quotes when given' do
    reply = Reply.new owner, 'text', 5, 6, 7
    expect(reply.quotes).not_to be_empty
  end

  describe '#to_string' do
    it 'should return correct string' do
      expect(reply.to_string).to eq " reply owner: john\n reply text: text"
    end
  end
end
