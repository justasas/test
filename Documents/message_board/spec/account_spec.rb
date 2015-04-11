require 'simplecov'
SimpleCov.start

require 'spec_helper'

RSpec::Matchers.define :have_a_correct_email_syntax do
  match do |email|
    email =~ /^[a-zA-Z0-9_.+\-]+@[a-zA-Z0-9\-]+\.[a-zA-Z0-9\-.]+$/
  end
end

describe Account do
  let(:user) { FactoryGirl.create(:account) }

  before(:all) { Account.destroy_all }
  after(:each) { Account.destroy_all }

  it 'should have name' do
    expect(user.name).to eq Account.last.name
  end

  it 'should have pass' do
    expect(user.pass).to eq 'password123'
  end

  # it "should have birth year" do
  #   expect(user.created_at).to eq 1993
  # end

  it 'should have correct email syntax' do
    expect(user.email).to have_a_correct_email_syntax
  end
end
