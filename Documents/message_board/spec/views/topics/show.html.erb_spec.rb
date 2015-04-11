require 'rails_helper'

RSpec.describe 'topics/show.html.erb', type: :view do
  it 'should display topic correctly' do
    assign(:topic, FactoryGirl.create(:topic_with_reply,
                                      title: 'verynicetitle'))

    render

    expect(rendered).to match(/verynicetitle/)
    expect(rendered).to match(/Commenter/)
  end

  it "should display 'log in if you want to reply' if user_id is nil" do
    assign(:topic, FactoryGirl.create(:topic_with_reply,
                                      title: 'verynicetitle'))
    render
    expect(rendered).to match(/log in/)
    expect(rendered).to match(/if you want to reply/)
  end

  it 'should display reply post form if user_id is not nil' do
    assign(:topic, FactoryGirl.create(:topic_with_reply,
                                      title: 'verynicetitle'))
    session[:user_id] = 10
    render
    expect(rendered).not_to match(/log in/)
    expect(rendered).to match(/form/)
  end
end
