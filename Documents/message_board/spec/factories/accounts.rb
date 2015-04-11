FactoryGirl.define do
  factory :account do |u|
    u.sequence(:name) { |n| "name12#{n}" }
    u.sequence(:email) { |n| "email12#{n}@email.com" }
    u.pass 'password123'
  end
end
